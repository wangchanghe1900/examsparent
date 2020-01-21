package cn.unicom.exams.service.service.impl;

import cn.unicom.exams.model.entity.SysDept;
import cn.unicom.exams.model.vo.DeptInfo;
import cn.unicom.exams.service.mapper.SysDeptMapper;
import cn.unicom.exams.service.service.ISysDeptService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
     @Autowired
     private SysDeptMapper deptMapper;

    @Override
    public List<DeptInfo> getAllDeptInfo() {
        QueryWrapper<SysDept> queryWrapper=new QueryWrapper<>();
        queryWrapper.ne("del_flag",1)
                .orderByAsc("parent_id")
                .orderByAsc("order_num");
        List<SysDept> sysDepts = deptMapper.selectList(queryWrapper);

        return filterDeptList(0L,sysDepts);
    }

    private List<DeptInfo> filterDeptList(Long pId,List<SysDept> deptList){
        List<SysDept> subDepts = deptList.stream().filter(d -> d.getParentId() == pId).collect(Collectors.toList());
        List<DeptInfo> depts=new ArrayList<>();
        for(SysDept dept: subDepts){
            DeptInfo info=new DeptInfo();
            info.setId(dept.getId());
            info.setTitle(dept.getDeptname());
            info.setContent(dept.getContent());
            info.setOrderNum(dept.getOrderNum());
            info.setParentId(dept.getParentId());
            info.setSpread(true);
            info.setChecked(false);
            info.setChildren(filterDeptList(info.getId(),deptList));
            depts.add(info);
        }
        return depts;
    }
}
