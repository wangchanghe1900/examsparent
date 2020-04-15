package cn.unicom.exams.web.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.unicom.exams.model.entity.*;
import cn.unicom.exams.model.vo.*;
import cn.unicom.exams.model.web.Response;
import cn.unicom.exams.model.web.WebResponse;
import cn.unicom.exams.service.service.*;
import cn.unicom.exams.service.service.impl.SysUserServiceImpl;
import cn.unicom.exams.web.utils.ButtonAuthorUtils;
import cn.unicom.exams.web.utils.ShiroUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.security.spec.ECGenParameterSpec;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author 王长何
 * @create 2020-03-19 17:04
 */
@Controller
@RequestMapping("/notice")
@Slf4j
public class NoticeController {
    @Autowired
    private ISysNoticeService noticeService;

    @Autowired
    private ButtonAuthorUtils buttonAuthorUtils;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysEmployeeService employeeService;

    @Autowired
    private ISysUsermessagesService usermessagesService;

    @Autowired
    private ISysDeptService deptService;


    @GetMapping("/noticeList")
    @RequiresPermissions("notice:list")
    public String noticeList(){
        return "notice/noticeList";
    }


    @GetMapping("/getNoticeList")
    @RequiresPermissions("notice:find")
    @ResponseBody
    public WebResponse getNoticeList(int page, int limit,NoticeVo noticeVo){
        try{
            IPage<SysNotice> iPage = noticeService.getSysNoticeInfoByCondition(page, limit, noticeVo);
            ButtonInfo notictButtons = buttonAuthorUtils.getButtonAuthority("notice");
            List<NoticeInfo> noticeInfoList=new ArrayList<>();
            Long recourdCount=iPage.getTotal();
            for(SysNotice notice:iPage.getRecords()){
                NoticeInfo info=new NoticeInfo();
                String depts=notice.getDeptName();
                String[] deptArr=depts.split(",");
                List<Long> ids=new ArrayList<>();
                for(String dept:deptArr){
                    QueryWrapper<SysDept> queryWrapper=new QueryWrapper<>();
                    queryWrapper.eq("deptname",dept);
                    SysDept sysDept = deptService.getOne(queryWrapper);
                    ids.add(sysDept.getId());
                }
                info.setDeptIds(ids);
                BeanUtil.copyProperties(notice,info);
                info.setIsEdit(notictButtons.getIsEdit());
                info.setIsDel(notictButtons.getIsDel());
                info.setIsDetail(notictButtons.getIsDetail());
                noticeInfoList.add(info);
            }
            return new WebResponse(0,"",recourdCount.intValue(),noticeInfoList);
        }catch(Exception e){
            log.error(e.getMessage());
            return new WebResponse(500,"提取数据失败",0);
        }

    }

    @GetMapping("/addNoticeList")
    @RequiresPermissions("notice:add")
    public String addNoticeList(){
        return "notice/noticeAdd";
    }

    @PostMapping("/saveNotice")
    @ResponseBody
    public Response saveNotice(String infos){
        Subject subject = ShiroUtils.getSubject();
        UserInfo user = (UserInfo) subject.getPrincipal();
        try {
            infos="["+infos+"]";
            List<NoticeVo> noticeVoList = JSON.parseArray(infos, NoticeVo.class);
            if(noticeVoList!=null && noticeVoList.size()>0){
                NoticeVo noticeVo = noticeVoList.get(0);
                List<DeptInfo> deptInfoList = JSON.parseArray(noticeVo.getDeptName(), DeptInfo.class);
                List<DeptInfo> childrenInfos=null;
                Integer receviceCount=0;
                for(DeptInfo dept:deptInfoList){
                    childrenInfos= getChildrenDeptInfo(dept.getChildren());
                    String deptName="";
                    for(DeptInfo info:childrenInfos){
                        deptName+=info.getTitle()+",";
                    }
                    deptName=deptName.substring(0,deptName.length()-1);
                    noticeVo.setDeptName(deptName);
                }
                if(noticeVo.getIsSendSysUser()!=null){
                    Integer userCount=0;
                    for(DeptInfo info:childrenInfos){
                        QueryWrapper<SysUser> queryWrapper=new QueryWrapper<>();
                        queryWrapper.eq("status",1).eq("dept_id",info.getId());
                        userCount+=userService.count(queryWrapper);
                    }
                    receviceCount+=userCount;
                    noticeVo.setIsSendSysUser("是");

                }else{
                    noticeVo.setIsSendSysUser("否");
                }

                if(noticeVo.getIsSendEmp()!=null){
                    Integer empCount=0;
                    for(DeptInfo info:childrenInfos){
                        QueryWrapper<SysEmployee> queryWrapper=new QueryWrapper<>();
                        queryWrapper.eq("employeeStatus","正常").eq("dept_id",info.getId());
                        empCount+=employeeService.count(queryWrapper);
                    }
                    receviceCount+=empCount;
                    noticeVo.setIsSendEmp("是");
                }else{
                    noticeVo.setIsSendEmp("否");
                }

                noticeVo.setReceiveCount(receviceCount);
                noticeVo.setReaderCount(0);
                if(noticeVo.getId()==null){
                    noticeVo.setCreateTime(LocalDateTime.now());
                    noticeVo.setCreateUser(user.getRealname());
                    noticeService.save(noticeVo);
                    if(("发布").equals(noticeVo.getStatus())){
                        sendMessages(childrenInfos,noticeVo,user);
                    }

                }else{
                    noticeService.updateById(noticeVo);
                    QueryWrapper<SysUsermessages> queryWrapper=new QueryWrapper<>();
                    queryWrapper.eq("notice_id",noticeVo.getId());
                    usermessagesService.remove(queryWrapper);
                    if(("发布").equals(noticeVo.getStatus())){
                        sendMessages(childrenInfos,noticeVo,user);
                    }
                }

            }
            return new Response(200,"保存通知信息成功！");
        }catch (Exception e){
            log.error(e.getMessage());
            return new Response(500,"保存通知信息错误！");
        }
    }

    private List<DeptInfo> getChildrenDeptInfo(List<DeptInfo> deptList){
        List<DeptInfo> deptInfos=new ArrayList<>();
        for(DeptInfo dept: deptList){
            if(dept.getChildren().size()>0){
                List<DeptInfo> childrenDeptInfo = getChildrenDeptInfo(dept.getChildren());
                if(childrenDeptInfo.size()>0){
                    deptInfos=childrenDeptInfo;
                }
            }else{
                deptInfos.add(dept);
            }

        }
        return deptInfos;
    }

    @GetMapping("/delNoticeById")
    @RequiresPermissions("notice:delete")
    @ResponseBody
    public Response delNoticeById(Long id){
        try{
            noticeService.removeById(id);
            QueryWrapper<SysUsermessages> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("notice_id",id);
            usermessagesService.remove(queryWrapper);
            return new Response(200,"删除公告信息成功");
        }catch (Exception e){
            log.error(e.getMessage());
            return new Response(500,"删除公告信息失败");
        }
    }

    @PostMapping("/delNoticeByIds")
    @RequiresPermissions("notice:batchdel")
    @ResponseBody
    public Response delNoticeByIds(String ids){
        try{
            String[] idArr = ids.split(",");
            List<Long> idList=new ArrayList<>();
            for(String id:idArr){
                idList.add(Long.parseLong(id));
            }
            noticeService.removeByIds(idList);
            for(Long id:idList){
                QueryWrapper<SysUsermessages> queryWrapper=new QueryWrapper<>();
                queryWrapper.eq("notice_id",id);
                usermessagesService.remove(queryWrapper);
            }
            return new Response(200,"删除公告信息成功！");
        }catch (Exception e){
            log.error(e.getMessage());
            return new Response(500,"删除公告信息失败");
        }
    }

    @GetMapping("/editNoticeList")
    @RequiresPermissions("notice:edit")
    public String editNoticeList(){
        return "notice/noticeAdd";
    }

    private void sendMessages(List<DeptInfo> deptList,NoticeVo noticeVo,SysUser user){
        List<SysUsermessages> usermessagesList=new ArrayList<>();

        for(DeptInfo deptInfo:deptList){
            if(("是").equals(noticeVo.getIsSendSysUser())){
                QueryWrapper<SysUser> queryWrapper=new QueryWrapper<>();
                queryWrapper.eq("dept_id",deptInfo.getId());
                List<SysUser> userList = userService.list(queryWrapper);
                for(SysUser sysUser:userList){
                    SysUsermessages usermessages=new SysUsermessages();
                    usermessages.setReceviceUserCode(sysUser.getId());
                    usermessages.setNoticeId(noticeVo.getId());
                    usermessages.setSendUser(user.getRealname());
                    usermessages.setIsRead("否");
                    usermessages.setCreateDate(LocalDateTime.now());
                    usermessagesList.add(usermessages);
                }
            }
            if(("是").equals(noticeVo.getIsSendEmp())){
                QueryWrapper<SysEmployee> queryWrapper=new QueryWrapper<>();
                queryWrapper.eq("dept_id",deptInfo.getId());
                List<SysEmployee> employeeList = employeeService.list(queryWrapper);
                for(SysEmployee employee:employeeList){
                    SysUsermessages empmessages=new SysUsermessages();
                    empmessages.setReceviceUserCode(Long.parseLong(employee.getEmployeeCode()));
                    empmessages.setNoticeId(noticeVo.getId());
                    empmessages.setSendUser(user.getRealname());
                    empmessages.setIsRead("否");
                    empmessages.setCreateDate(LocalDateTime.now());
                    usermessagesList.add(empmessages);
                }

            }
        }
        for(SysUsermessages usermessages:usermessagesList){
            usermessagesService.save(usermessages);

        }
    }

    @GetMapping("/publishNotice")
    @ResponseBody
    @RequiresPermissions("notice:publish")
    public Response publishNotice(Long id,String status){
        try{
            Subject subject = ShiroUtils.getSubject();
            UserInfo user = (UserInfo) subject.getPrincipal();
            SysNotice notice=new SysNotice();
            notice.setId(id);
            notice.setStatus(status);
            if("未发布".equals(status)){
                QueryWrapper<SysUsermessages> queryWrapper=new QueryWrapper<>();
                queryWrapper.eq("notice_id",id);
                usermessagesService.remove(queryWrapper);
            }else{
                QueryWrapper<SysUsermessages> queryWrapper=new QueryWrapper<>();
                queryWrapper.eq("notice_id",id);
                usermessagesService.remove(queryWrapper);
                SysNotice sysNotice = noticeService.getById(id);
                String[] deptArr=sysNotice.getDeptName().split(",");
                List<DeptInfo> deptList=new ArrayList<>();
                for(String dept:deptArr){
                    QueryWrapper<SysDept> qw=new QueryWrapper<>();
                    qw.eq("deptname",dept);
                    SysDept sysDept = deptService.getOne(qw);
                    DeptInfo info=new DeptInfo();
                    info.setId(sysDept.getId());
                    deptList.add(info);
                }
                NoticeVo noticeVo=new NoticeVo();
                BeanUtil.copyProperties(sysNotice,noticeVo);
                sendMessages(deptList, noticeVo,user);
            }
            noticeService.updateById(notice);
            return new Response(200,"状态更新成功！");
        }catch (Exception e){
            log.error(e.getMessage());
            return new Response(500,"状态更新失败！");
        }
    }

    @GetMapping("/showDetailList")
    @RequiresPermissions("notice:detail")
    public String showDetailList(){
        return "notice/noticeDetail";
    }

    @PostMapping("/uploadImgFile")
    @ResponseBody
    public Response uploadImgFile(@RequestParam("file") MultipartFile[] files, HttpServletRequest request){
        try{
            String[] pathArr=new String[files.length];
            int i=0;
            for(MultipartFile file : files){
                String filePath=saveUploadFile("/upload/noticeFile/",file,request);
                pathArr[i]=filePath;
                i++;
            }
            String path="";
            for(String p: pathArr){
               path +="/examsweb"+ p +",";
            }
            path=path.substring(0,path.length()-1);
            Map<String,String> map=new HashMap<>();
            map.put("src",path);
            return new Response(0,"",map);
        }catch (Exception e){
            log.error("图片传输失败:"+e.getMessage());
            return new Response(500,"图片传输失败！");
        }

    }
    private String saveUploadFile(String savePath, MultipartFile fileinfo,  HttpServletRequest request) throws Exception{
        DateTimeFormatter df=DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate now=LocalDate.now();
        String path=savePath+now.format(df);
        String realPath = request.getServletContext().getRealPath(path);//context.getRealPath("c:/upload/commFile");
        File dir=new File(realPath);
        if(!dir.exists()){
            dir.mkdirs();
        }
        String filename = fileinfo.getOriginalFilename();
        filename = UUID.randomUUID().toString()+ filename.substring(filename.lastIndexOf("."));
        File f= new File(realPath, filename);
        fileinfo.transferTo(f);
        return path+"/"+filename;
    }

}
