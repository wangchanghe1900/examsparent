package cn.unicom.exams.service.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.unicom.exams.model.entity.*;
import cn.unicom.exams.model.vo.DeptInfo;
import cn.unicom.exams.model.vo.NoticeVo;
import cn.unicom.exams.model.vo.UserInfo;
import cn.unicom.exams.service.mapper.*;
import cn.unicom.exams.service.service.ISysNoticeService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.security.auth.Subject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 王长何
 * @since 2020-03-19
 */
@Service
public class SysNoticeServiceImpl extends ServiceImpl<SysNoticeMapper, SysNotice> implements ISysNoticeService {
    @Resource
     private SysNoticeMapper sysNoticeMapper;

    @Resource
    private SysUserMapper userMapper;

    @Resource
    private SysEmployeeMapper employeeMapper;

    @Resource
    private SysUsermessagesMapper usermessagesMapper;

    @Resource
    private SysDeptMapper deptMapper;

    @Override
    public IPage<SysNotice> getSysNoticeInfoByCondition(int page, int limit, NoticeVo noticeVo) throws Exception {
        Page<SysNotice> iPage=new Page<>(page,limit);
        QueryWrapper<SysNotice> queryWrapper=new QueryWrapper<>();
        if(noticeVo != null){
            queryWrapper.likeRight(StringUtils.isNotEmpty(noticeVo.getTitle()),"title",noticeVo.getTitle());
        }
        queryWrapper.orderByDesc("createTime");
        return sysNoticeMapper.selectPage(iPage,queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveNoticeInfo(String infos,String createUser) throws Exception {
        NoticeVo noticeVo = JSON.parseObject(infos, NoticeVo.class);
        List<DeptInfo> deptInfoList = JSON.parseArray(noticeVo.getDeptName(), DeptInfo.class);
        //System.out.println("deptInfoList = " + deptInfoList);
        List<DeptInfo> childrenInfos=new ArrayList<>();
        Integer receviceCount=0;
        for(DeptInfo dept:deptInfoList){
           getChildrenDeptInfo(dept.getChildren(),childrenInfos);
            String deptName="";
            for(DeptInfo info:childrenInfos){
                deptName+=info.getId()+",";
            }
            deptName=deptName.substring(0,deptName.length()-1);
            noticeVo.setDeptName(deptName);
        }
        if(noticeVo.getIsSendSysUser()!=null){
            Integer userCount=getReceviceMsgCount(childrenInfos,"User");
            receviceCount+=userCount;
            noticeVo.setIsSendSysUser("是");
            childrenInfos.removeIf(dept->dept.getParentId()==null);
        }else{
            noticeVo.setIsSendSysUser("否");
        }

        if(noticeVo.getIsSendEmp()!=null){
            Integer empCount=getReceviceMsgCount(childrenInfos,"Emp");;
            receviceCount+=empCount;
            noticeVo.setIsSendEmp("是");
            childrenInfos.removeIf(dept->dept.getParentId()==null);
        }else{
            noticeVo.setIsSendEmp("否");
        }

        noticeVo.setReceiveCount(receviceCount);
        noticeVo.setReaderCount(0);
        if(noticeVo.getId()==null){
            noticeVo.setCreateTime(LocalDateTime.now());
            noticeVo.setCreateUser(createUser);
            sysNoticeMapper.insert(noticeVo);
            if(("发布").equals(noticeVo.getStatus())){
                sendMessages(childrenInfos,noticeVo,createUser);
            }

        }else{
            sysNoticeMapper.updateById(noticeVo);
            QueryWrapper<SysUsermessages> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("notice_id",noticeVo.getId());
            usermessagesMapper.delete(queryWrapper);
            if(("发布").equals(noticeVo.getStatus())){
                sendMessages(childrenInfos,noticeVo,createUser);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void publishNotice(Long id, String status,String userName) throws Exception {
        SysNotice notice=new SysNotice();
        notice.setId(id);
        notice.setStatus(status);
        if("未发布".equals(status)){
            QueryWrapper<SysUsermessages> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("notice_id",id);
            usermessagesMapper.delete(queryWrapper);
        }else{
            QueryWrapper<SysUsermessages> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("notice_id",id);
            usermessagesMapper.delete(queryWrapper);
            SysNotice sysNotice = sysNoticeMapper.selectById(id);
            String[] deptArr=sysNotice.getDeptName().split(",");
            List<DeptInfo> deptList=new ArrayList<>();
            for(String deptid:deptArr){
                DeptInfo info=new DeptInfo();
                info.setId(Long.parseLong(deptid));
                deptList.add(info);
            }
            NoticeVo noticeVo=new NoticeVo();
            BeanUtil.copyProperties(sysNotice,noticeVo);
            sendMessages(deptList, noticeVo,userName);
        }
        sysNoticeMapper.updateById(notice);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteNotice(Long id) throws Exception {
        sysNoticeMapper.deleteById(id);
        QueryWrapper<SysUsermessages> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("notice_id",id);
        usermessagesMapper.delete(queryWrapper);
    }

    private void getChildrenDeptInfo(List<DeptInfo> deptList,List<DeptInfo> resultList){
        for(DeptInfo dept: deptList){
            if(dept.getChildren().size()>0){
                getChildrenDeptInfo(dept.getChildren(),resultList);
            }else{
                //deptInfos.add(dept);
                resultList.add(dept);
            }

        }
        //return deptInfos;
    }

    private void sendMessages(List<DeptInfo> deptList,NoticeVo noticeVo,String createUser){
        List<SysDept> deptInfoList=deptMapper.selectList(null);
        List<SysUsermessages> usermessagesList=new ArrayList<>();
        Set<Long> parentIds=new HashSet<>();
        for(DeptInfo info:deptList){
            List<SysDept> depts=deptInfoList.stream().filter(dept -> dept.getId().intValue() == info.getId().intValue()).collect(Collectors.toList());
            for(SysDept d:depts){
                parentIds.add(d.getParentId());
            }
        }
        for(Long id:parentIds){
            DeptInfo dp=new DeptInfo();
            dp.setId(id);
            deptList.add(dp);
        }

        for(DeptInfo deptInfo:deptList){
            if(("是").equals(noticeVo.getIsSendSysUser())){
                QueryWrapper<SysUser> queryWrapper=new QueryWrapper<>();
                queryWrapper.eq("dept_id",deptInfo.getId());
                List<SysUser> userList = userMapper.selectList(queryWrapper);
                for(SysUser sysUser:userList){
                    SysUsermessages usermessages=new SysUsermessages();
                    usermessages.setReceviceUserCode(sysUser.getId());
                    usermessages.setNoticeId(noticeVo.getId());
                    usermessages.setSendUser(createUser);
                    usermessages.setIsRead("否");
                    usermessages.setCreateDate(LocalDateTime.now());
                    usermessagesList.add(usermessages);
                }
            }
            if(("是").equals(noticeVo.getIsSendEmp())){
                QueryWrapper<SysEmployee> queryWrapper=new QueryWrapper<>();
                queryWrapper.eq("dept_id",deptInfo.getId());
                List<SysEmployee> employeeList = employeeMapper.selectList(queryWrapper);
                for(SysEmployee employee:employeeList){
                    SysUsermessages empmessages=new SysUsermessages();
                    empmessages.setReceviceUserCode(Long.parseLong(employee.getEmployeeCode()));
                    empmessages.setNoticeId(noticeVo.getId());
                    empmessages.setSendUser(createUser);
                    empmessages.setIsRead("否");
                    empmessages.setCreateDate(LocalDateTime.now());
                    usermessagesList.add(empmessages);
                }

            }
        }
        for(SysUsermessages usermessages:usermessagesList){
            usermessagesMapper.insert(usermessages);
        }
    }

    private Integer getReceviceMsgCount(List<DeptInfo> deptList,String flag) throws Exception{
        List<SysDept> deptInfoList=deptMapper.selectList(null);
        Set<Long> parentIds=new HashSet<>();
        Integer count=0;
        for(DeptInfo info:deptList){
            List<SysDept> depts=deptInfoList.stream().filter(dept -> dept.getId().intValue() == info.getId().intValue()).collect(Collectors.toList());
            for(SysDept d:depts){
                parentIds.add(d.getParentId());
            }
        }
        for(Long id:parentIds){
            DeptInfo dp=new DeptInfo();
            dp.setId(id);
            deptList.add(dp);
        }

        for(DeptInfo info:deptList){
            if("User".equals(flag)){
                QueryWrapper<SysUser> queryWrapper=new QueryWrapper<>();
                queryWrapper.eq("status",1).eq("dept_id",info.getId());
                count += userMapper.selectCount(queryWrapper);
            }
            if("Emp".equals(flag)){
                QueryWrapper<SysEmployee> queryWrapper=new QueryWrapper<>();
                queryWrapper.eq("employeeStatus","正常").eq("dept_id",info.getId());
                count+=employeeMapper.selectCount(queryWrapper);
            }

        }

        return count;
    }
}
