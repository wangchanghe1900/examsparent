package cn.unicom.exams.web.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.unicom.exams.model.entity.SysDept;
import cn.unicom.exams.model.entity.SysNotice;
import cn.unicom.exams.model.vo.ButtonInfo;
import cn.unicom.exams.model.vo.NoticeInfo;
import cn.unicom.exams.model.vo.NoticeVo;
import cn.unicom.exams.model.vo.UserInfo;
import cn.unicom.exams.model.web.Response;
import cn.unicom.exams.model.web.WebResponse;
import cn.unicom.exams.service.service.ISysDeptService;
import cn.unicom.exams.service.service.ISysNoticeService;
import cn.unicom.exams.service.service.ISysUsermessagesService;
import cn.unicom.exams.web.utils.ButtonAuthorUtils;
import cn.unicom.exams.web.utils.ShiroUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
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
    @Value("${exams.uploadPath}")
    private String uploadPath;

    @Value("${server.servlet.context-path}")
    private String webPath;

    @Autowired
    private ISysNoticeService noticeService;

    @Autowired
    private ButtonAuthorUtils buttonAuthorUtils;


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
                List<String> deptList=new ArrayList<>();
                List<Long> idList=new ArrayList<>();
                for(String id:deptArr){
                    QueryWrapper<SysDept> queryWrapper=new QueryWrapper<>();
                    queryWrapper.eq("id",Long.parseLong(id));
                    SysDept sysDept = deptService.getOne(queryWrapper);
                    deptList.add(sysDept.getDeptname());
                    idList.add(Long.parseLong(id));
                }
                info.setDeptList(deptList);
                info.setDeptIds(idList);
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
        try {
            Subject subject = ShiroUtils.getSubject();
            UserInfo user = (UserInfo) subject.getPrincipal();
            noticeService.saveNoticeInfo(infos,user.getRealname());
            return new Response(200,"保存通知信息成功！");
        }catch (Exception e){
            log.error(e.getMessage());
            return new Response(500,"保存通知信息错误！");
        }
    }



    @GetMapping("/delNoticeById")
    @RequiresPermissions("notice:delete")
    @ResponseBody
    public Response delNoticeById(Long id){
        try{
            noticeService.deleteNotice(id);
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
            for(String id:idArr){
                noticeService.deleteNotice(Long.parseLong(id));
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
        return "notice/noticeEdit";
    }



    @GetMapping("/publishNotice")
    @ResponseBody
    @RequiresPermissions("notice:publish")
    public Response publishNotice(Long id,String status){
        try{
            Subject subject = ShiroUtils.getSubject();
            UserInfo user = (UserInfo) subject.getPrincipal();
            noticeService.publishNotice(id,status,user.getRealname());
            String msg="发布".equals(status) ? "通知发布成功":"通知取消成功";
            return new Response(200,msg);
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
    public Response uploadImgFile(@RequestParam("file") MultipartFile[] files){
        try{
            String[] pathArr=new String[files.length];
            int i=0;
            for(MultipartFile file : files){
                String filePath=saveUploadFile("/noticeFile/",file);
                pathArr[i]=filePath;
                i++;
            }
            String path="";
            for(String p: pathArr){
               path +=webPath+ p +",";
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
    private String saveUploadFile(String savePath, MultipartFile fileinfo) throws Exception{
        DateTimeFormatter df=DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate now=LocalDate.now();
        String path=savePath+now.format(df);
        String realPath =uploadPath+path ;//request.getServletContext().getRealPath(path);//context.getRealPath("c:/upload/commFile");
        File dir=new File(realPath);
        if(!dir.exists()){
            dir.mkdirs();
        }
        String filename = fileinfo.getOriginalFilename();
        filename = UUID.randomUUID().toString()+ filename.substring(filename.lastIndexOf("."));
        File f= new File(realPath, filename);
        fileinfo.transferTo(f);
        return "/upload"+path+"/"+filename;
    }

    @GetMapping("/getNoticeById")
    @ResponseBody
    public Response getNoticeById(Long id){
        try{
            SysNotice sysNotice = noticeService.getById(id);
            List<Long> idList=new ArrayList<>();
            NoticeInfo info=new NoticeInfo();
            String depts=sysNotice.getDeptName();
            String[] deptArr=depts.split(",");
            for(String deptid:deptArr){
                idList.add(Long.parseLong(deptid));
            }
            info.setDeptIds(idList);
            BeanUtil.copyProperties(sysNotice,info);
            return new Response(200,"",info);
        }catch (Exception e){
            return new Response(500,"通知数据提取错误");
        }
    }

}
