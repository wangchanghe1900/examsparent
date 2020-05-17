package cn.unicom.exams.web.controller;

import cn.unicom.exams.model.vo.ButtonInfo;
import cn.unicom.exams.model.vo.TestPaperInfo;
import cn.unicom.exams.model.vo.TestPaperVo;
import cn.unicom.exams.model.vo.UserInfo;
import cn.unicom.exams.model.web.Response;
import cn.unicom.exams.model.web.WebResponse;
import cn.unicom.exams.service.service.ISysTestpaperService;
import cn.unicom.exams.web.utils.ButtonAuthorUtils;
import cn.unicom.exams.web.utils.ShiroUtils;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author 王长何
 * @create 2020-03-02 17:44
 */
@Controller
@RequestMapping("/test")
@Slf4j
public class TestPaperController {
    private  final static String  UPLOAD_IMG_PATH="/imgFile/";
    @Value("${exams.uploadPath}")
    private String uploadPath;

    @Autowired
    private ISysTestpaperService testpaperService;

    @Autowired
    private ButtonAuthorUtils buttonAuthorUtils;

    @GetMapping("/testList")
    @RequiresPermissions("test:list")
    public String testList(){
        return "test/testList";
    }

    @GetMapping("/testAdd")
    @RequiresPermissions("test:add")
    public String testAdd(){
        return "test/testAdd";
    }

    @GetMapping("/getTestList")
    @RequiresPermissions("test:find")
    @ResponseBody
    public WebResponse getTestList(int page, int limit, TestPaperVo testPaperVo){
        Subject subject = ShiroUtils.getSubject();
        UserInfo user = (UserInfo) subject.getPrincipal();
        testPaperVo.setUserName(user.getUsername());
        testPaperVo.setDeptId(user.getDeptId());
        try{
            IPage<TestPaperInfo> testPaperInfoByPage = testpaperService.getTestPaperInfoByPage(page, limit, testPaperVo);
            ButtonInfo testpower = buttonAuthorUtils.getButtonAuthority("test");
            for (int i = 0; i <testPaperInfoByPage.getRecords().size() ; i++) {
                testPaperInfoByPage.getRecords().get(i).setIsEdit(testpower.getIsEdit());
                testPaperInfoByPage.getRecords().get(i).setIsDel(testpower.getIsDel());

            }
            Long count=testPaperInfoByPage.getTotal();
            return new WebResponse(0,"",count.intValue(),testPaperInfoByPage.getRecords());
        }catch (Exception e){
            log.error(e.getMessage());
            return new WebResponse(500,"提取试卷信息失败，系统错误",0);
        }

    }

    @GetMapping("/checkResourceList")
    public String checkResourceList(){
        return "test/resourceList";
    }

    @PostMapping("/testInfoSave")
    @ResponseBody
    public Response testInfoSave(String  testInfo){
        try{
            testInfo="["+testInfo+"]";
            List<TestPaperVo> testPaperVoList = JSON.parseArray(testInfo, TestPaperVo.class);
            TestPaperVo testPaperVo=testPaperVoList.get(0);
            if(testPaperVo.getResId()==null){
                return  new Response(500,"所选考试资源不能为空!");
            }
            Subject subject = ShiroUtils.getSubject();
            UserInfo user = (UserInfo) subject.getPrincipal();
            //String urlPath=saveUploadFile(UPLOAD_IMG_PATH,imginfo);
            if(testPaperVo.getId()==null){
                testPaperVo.setExamsStartTime(testPaperVo.getStartDate());
                testPaperVo.setExamsEndTime(testPaperVo.getEndDate());
                testPaperVo.setCreateTime(LocalDateTime.now());
                testPaperVo.setUpdateTime(LocalDateTime.now());
                testPaperVo.setCreateUser(user.getRealname());
                if(testPaperVo.getTestStatus()==null){
                    testPaperVo.setTestStatus("未发布");
                }else{
                    testPaperVo.setTestStatus("发布");
                }
                testPaperVo.setImgUrl(testPaperVo.getImgUrl());
                testpaperService.save(testPaperVo);
            }else{
/*                if(testPaperVo.getImgUrl()!=null) {
                    String deletePath =uploadPath +testPaperVo.getImgUrl();//request.getServletContext().getRealPath(testPaperVo.getImgUrl());
                    File file = new File(deletePath);
                    file.delete();
                }*/
                testPaperVo.setExamsStartTime(testPaperVo.getStartDate());
                testPaperVo.setExamsEndTime(testPaperVo.getEndDate());
                testPaperVo.setUpdateTime(LocalDateTime.now());
                if(testPaperVo.getTestStatus()==null){
                    testPaperVo.setTestStatus("未发布");
                }else{
                    testPaperVo.setTestStatus("发布");
                }
                testPaperVo.setImgUrl(testPaperVo.getImgUrl());
                testpaperService.updateById(testPaperVo);
            }
            testpaperService.publishTest(testPaperVo.getId(),testPaperVo.getTestStatus());
            return new Response(0,"考试信息保存成功！");
        }catch (Exception e){
            log.error(e.getMessage());
            return  new Response(500,"保存考试信息时系统错误");
        }
    }

    private String saveUploadFile(String savePath,  MultipartFile imginfo) throws Exception{

        DateTimeFormatter df=DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate now=LocalDate.now();
        String path=uploadPath+savePath+now.format(df);
        String urlPath=savePath + now.format(df);
        String realPath =path; //request.getServletContext().getRealPath(path);//context.getRealPath("c:/upload/commFile");
        File dir=new File(realPath);
        if(!dir.exists()){
            dir.mkdirs();
        }
        String filename = imginfo.getOriginalFilename();
        filename = UUID.randomUUID().toString()+ filename.substring(filename.lastIndexOf("."));
        File f= new File(realPath, filename);
        imginfo.transferTo(f);
        return urlPath+"/"+filename;

    }
    @PostMapping("/delTestInfoByIds")
    @RequiresPermissions("test:batchdel")
    @ResponseBody
    public Response delTestInfoByIds(String ids,String files){
        try{
            String[] idArr=ids.split(",");
            String[] filepaths=files.split(",");
            for(String filePath:filepaths){
                String realPath = uploadPath+filePath;//request.getServletContext().getRealPath(filePath);
                File file=new File(realPath);
                file.delete();
            }
            List<Integer> idList=new ArrayList<>();
            for(String s: idArr){
                idList.add(Integer.valueOf(s));
            }
            testpaperService.removeByIds(idList);
            return new Response(200,"批量删除成功");
        }catch (Exception e){
            log.error(e.getMessage());
            return new Response(500,"批量删除失败");
        }
    }

    @GetMapping("/delTestInfoById")
    @RequiresPermissions("test:delete")
    @ResponseBody
    public Response delTestInfoById(Integer id,String url){
        try{
            String realPath = uploadPath+url;//request.getServletContext().getRealPath(url);
            File file=new File(realPath);
            file.delete();
            testpaperService.removeById(id);
            return new Response(200,"考试信息删除成功");
        }catch (Exception e){
            log.error(e.getMessage());
            return new Response(500,"考试信息删除失败");
        }
    }

    @GetMapping("/editTestInfoList")
    @RequiresPermissions("test:edit")
    public String editTestInfoList(){
        return "test/testAdd";
    }

    @GetMapping("/publishTest")
    @ResponseBody
    @RequiresPermissions("test:publish")
    public Response publishTest(Long id,String status){
        try{
            testpaperService.publishTest(id,status);
            return new Response(200,status);
        }catch (Exception e){
            log.error(e.getMessage());
            return new Response(500,"发布失败!");
        }
    }

    @GetMapping("getTestCount")
    @ResponseBody
    public Response getTestCount(){
        try{
            int count = testpaperService.count();
            return new Response(200,"",count);
        }catch (Exception e){
            log.error("提取系统考卷数量错误："+e.getMessage());
            return  new Response(500,"提取系统考卷数量错误");
        }
    }

    @PostMapping("/testimgupload")
    @ResponseBody
    public Response testimgupload(MultipartFile imginfo){
        String contentType = imginfo.getContentType();		//判断上传文件是否为图片		if (contentType==null||!contentType.startsWith("image/")) {			System.out.println("===不属于图片类型...===");			return;		}
        if (contentType==null||!contentType.startsWith("image")) {
            //System.out.println("===不符合上传类型...===");
            return  new Response(500,"资源不符合上传类型");
        }
        try{
            String urlPath=saveUploadFile(UPLOAD_IMG_PATH,imginfo);
            Map<String,String> map=new HashMap<>();
            map.put("src",urlPath);
            return  new Response(0,"",map);
        }catch (Exception e){
            return  new Response(500,"文件上传失败");
        }
    }

}
