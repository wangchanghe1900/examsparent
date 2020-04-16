package cn.unicom.exams.service.service.impl;

import cn.unicom.exams.model.entity.SysDept;
import cn.unicom.exams.model.vo.DeptEmpInfo;
import cn.unicom.exams.model.vo.DeptInfo;
import cn.unicom.exams.service.mapper.SysDeptMapper;
import cn.unicom.exams.service.service.ISysDeptService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * 部门管理 服务实现类
 * </p>
 *
 * @author 王长河
 * @since 2019-12-31
 */
@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements ISysDeptService {
     @Resource
     private SysDeptMapper deptMapper;

    @Override
    public List<DeptInfo> getAllDeptInfo() throws Exception{
        QueryWrapper<SysDept> queryWrapper=new QueryWrapper<>();
        queryWrapper.ne("del_flag",-1)
                .orderByAsc("parent_id")
                .orderByAsc("order_num");
        List<SysDept> sysDepts = deptMapper.selectList(queryWrapper);
        List<DeptEmpInfo> deptEmpInfoList = deptMapper.getAllDeptAndEmpInfo();
        return filterDeptList(0L,sysDepts);
    }

    @Override
    public List<DeptInfo> getAllDeptAndEmpInfo() throws Exception {
        QueryWrapper<SysDept> queryWrapper=new QueryWrapper<>();
        queryWrapper.ne("del_flag",-1)
                .orderByAsc("parent_id")
                .orderByAsc("order_num");
        List<SysDept> sysDepts = deptMapper.selectList(queryWrapper);
        List<DeptEmpInfo> deptEmpInfoList = deptMapper.getAllDeptAndEmpInfo();
        return filterDeptAndEmpList(0L,sysDepts,deptEmpInfoList);
    }

    private List<DeptInfo> filterDeptList(Long pId,List<SysDept> deptList){
        List<SysDept> subDepts = deptList.stream().filter(d -> d.getParentId().intValue() == pId.intValue()).collect(Collectors.toList());
        List<DeptInfo> depts=new ArrayList<>();
        for(SysDept dept: subDepts){
            DeptInfo info=new DeptInfo();
            //Optional<DeptEmpInfo> deptoptional = deptEmpInfoList.stream().filter(d -> d.getId().intValue() == dept.getId().intValue()).findFirst();
            info.setId(dept.getId());
            info.setTitle(dept.getDeptname());
            info.setContent(dept.getContent());
            info.setOrderNum(dept.getOrderNum());
            info.setParentId(dept.getParentId());
            Optional<SysDept> first = deptList.stream().filter(s -> s.getId().intValue() == dept.getParentId().intValue()).findFirst();
            if(!first.isPresent()){
                info.setParentName("");
            }else {
                info.setParentName(first.get().getDeptname());
            }
            info.setSpread(true);
            info.setChecked(false);
            //System.out.println("------------"+dept.getId()+"----------");
            info.setChildren(filterDeptList(dept.getId(),deptList));
            depts.add(info);
        }
        return depts;
    }

    private List<DeptInfo> filterDeptAndEmpList(Long pId,List<SysDept> deptList,List<DeptEmpInfo> deptEmpInfoList){
/*        QueryWrapper<SysDept> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("parent_id",pId);
        List<SysDept> subDepts = deptMapper.selectList(queryWrapper);*/

        List<SysDept> subDepts = deptList.stream().filter(d -> d.getParentId().intValue() == pId.intValue()).collect(Collectors.toList());
        List<DeptInfo> depts=new ArrayList<>();
        for(SysDept dept: subDepts){
            DeptInfo info=new DeptInfo();
            Optional<DeptEmpInfo> deptoptional = deptEmpInfoList.stream().filter(d -> d.getId().intValue() == dept.getId().intValue()).findFirst();
            info.setId(dept.getId());
            if(deptoptional.isPresent()){
                DeptEmpInfo deptEmpInfo = deptoptional.get();
                String countinfo=deptEmpInfo.getEmpList()==null?"(0)":"("+deptEmpInfo.getEmpList().size()+")";
                info.setTitle(dept.getDeptname()+countinfo);
            }else{
                info.setTitle(dept.getDeptname());
            }
            //info.setTitle(dept.getDeptname());
            info.setContent(dept.getContent());
            info.setOrderNum(dept.getOrderNum());
            info.setParentId(dept.getParentId());
            Optional<SysDept> first = deptList.stream().filter(s -> s.getId().intValue() == dept.getParentId().intValue()).findFirst();
            if(!first.isPresent()){
                info.setParentName("");
            }else {
                info.setParentName(first.get().getDeptname());
            }
            info.setSpread(false);
            info.setChecked(false);
            //System.out.println("------------"+dept.getId()+"----------");
            info.setChildren(filterDeptAndEmpList(dept.getId(),deptList,deptEmpInfoList));
            depts.add(info);
        }
        return depts;
    }
}
