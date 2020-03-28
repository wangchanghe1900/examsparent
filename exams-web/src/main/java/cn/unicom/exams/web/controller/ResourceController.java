package cn.unicom.exams.web.controller;

import cn.unicom.exams.model.vo.*;
import cn.unicom.exams.model.web.Response;
import cn.unicom.exams.model.web.WebResponse;
import cn.unicom.exams.service.service.ISysEmployeeService;
import cn.unicom.exams.service.service.ISysResourceinfoService;
import cn.unicom.exams.web.utils.ButtonAuthorUtils;
import cn.unicom.exams.web.utils.ShiroUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 王长何
 * @create 2020-02-25 12:17
 */
@Controller
@RequestMapping("/resource")
@Slf4j
public class ResourceController {
    @Autowired
    private ISysResourceinfoService resourceinfoService;

    @Autowired
    private ButtonAuthorUtils buttonAuthorUtils;

    @Autowired
    private ISysEmployeeService employeeService;

    @GetMapping("/commResourceList")
    public String commResourceList(){
        return "resource/commresourceList";
    }

    @GetMapping("/getCommResourceList")
    @ResponseBody
    public WebResponse getCommResourceList(int page, int limit, ResourceVo resourceVo){
        try{
            Subject subject = ShiroUtils.getSubject();
            UserInfo user = (UserInfo) subject.getPrincipal();
            resourceVo.setDeptId(user.getDeptId());
            resourceVo.setUserName(user.getUsername());
            IPage<ResourceInfo> resourceInfoByPage = resourceinfoService.getResourceInfoByPage(page, limit, resourceVo);
            Long count=resourceInfoByPage.getTotal();
            ButtonInfo resourcepower = buttonAuthorUtils.getButtonAuthority("commres");
            for (int i = 0; i < resourceInfoByPage.getRecords().size(); i++) {
                resourceInfoByPage.getRecords().get(i).setIsDel(resourcepower.getIsDel());
                resourceInfoByPage.getRecords().get(i).setIsDetail(resourcepower.getIsDetail());
                resourceInfoByPage.getRecords().get(i).setIsEdit(resourcepower.getIsEdit());
            }
            WebResponse response=new WebResponse(0,"",count.intValue(),resourceInfoByPage.getRecords());
            return response;
        }catch(Exception e){
            return new WebResponse(500,"系统错误",0);
        }
    }

    @GetMapping("/commResourceAdd")
    public String commResourceAdd(){
        return "resource/commonAdd";
    }

    @PostMapping("/commResourceFileUpload")
    @ResponseBody
    public Response commResourceFileUpload(ResourceVo resourceVo, MultipartFile fileinfo, HttpServletRequest request){
        if(resourceVo!=null){
            if("".equals(resourceVo.getResourceName())||"".equals(resourceVo.getRemark()==null)){
                return  new Response(500,"资源名称或备注为空");
            }
        }
        String contentType = fileinfo.getContentType();		//判断上传文件是否为图片		if (contentType==null||!contentType.startsWith("image/")) {			System.out.println("===不属于图片类型...===");			return;		}
        if (contentType==null||!contentType.startsWith("application/pdf")) {
            System.out.println("===不符合上传类型...===");
            return  new Response(500,"资源不符合上传类型");
        }
        try{
            saveUploadFile("/upload/commFile/",0,resourceVo,fileinfo,request);


        }catch(Exception e){
            log.error(e.getMessage());
           return new Response(500,"系统错误");
        }

        return  new Response(0,"资源上传成功");
    }

    @GetMapping("/editCommResourceList")
    public String editCommResourceList(){
        return "resource/commonEdit";
    }

    @PostMapping("/saveCommResource")
    @ResponseBody
    public Response saveCommResource(ResourceVo resourceVo){
        try {
            resourceVo.setUpdateTime(LocalDateTime.now());
            resourceinfoService.updateById(resourceVo);
            return new Response(200, "资源信息更新成功");
        }catch(Exception e){
            log.error(e.getMessage());
            return new Response(500, "系统错误");
        }

    }
    @PostMapping("/delResourceByIds")
    @ResponseBody
    public Response delResourceByIds(String ids,String filesPath,HttpServletRequest request){
        try{
            String[] arr=ids.split(",");
            List<Integer> resids=new ArrayList<>();
            for(String s: arr){
                resids.add(Integer.valueOf(s));
            }
            String[] fileArr=filesPath.split(",");
            for(String filePath:fileArr){
                String realPath = request.getServletContext().getRealPath(filePath);
                File file=new File(realPath);
                file.delete();
            }
            resourceinfoService.removeByIds(resids);
            return new Response(200,"删除成功");
        }catch (Exception e){
            log.error(e.getMessage());
            return new Response(500,"系统错误");
        }
    }

    @GetMapping("/delResourceById")
    @ResponseBody
    public Response delResourceById(Long id,String url,HttpServletRequest request){
        try{
            String realPath = request.getServletContext().getRealPath(url);
            File file=new File(realPath);
            file.delete();
            resourceinfoService.removeById(id);
            return new Response(200,"删除成功");
        }catch(Exception e){
            log.error(e.getMessage());
            return new Response(500,"系统错误");
        }
    }

    @PostMapping("/downloadResource")
    public String  downloadResource(String resourceName,String filePath,HttpServletRequest request, HttpServletResponse response){
        byte[] buffer = new byte[1024];
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        try{
            String realPath = request.getServletContext().getRealPath(filePath);
            File file = new File(realPath);
            response.setContentType("application/pdf");
            String fileName=URLEncoder.encode(resourceName+".pdf", "UTF-8");
            response.addHeader("Content-Disposition","attachment; filename="+fileName);
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            OutputStream os = response.getOutputStream();
            int i = bis.read(buffer);
            while (i != -1) {
                os.write(buffer, 0, i);
                i = bis.read(buffer);
            }

        }catch (Exception e){
           log.error(e.getMessage());
        }finally{
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                }
            }

        }
        return null;
    }

    @GetMapping("/commPDFList")
    public String commPDFList(){
        return "resource/commPDFList";
    }

    @GetMapping("/videoList")
    public String videoList(){
        return "resource/videoResourceList";
    }

    @GetMapping("/videoResourceAdd")
    public String  videoResourceAdd(ResourceVo resourceVo){
        return "resource/videoAdd";
    }

    @PostMapping("/videoResourceFileUpload")
    @ResponseBody
    public Response videoResourceFileUpload(ResourceVo resourceVo, MultipartFile fileinfo, HttpServletRequest request){
        if(resourceVo!=null){
            if("".equals(resourceVo.getResourceName())||"".equals(resourceVo.getRemark()==null)){
                return  new Response(500,"资源名称或备注为空");
            }
        }
        String contentType = fileinfo.getContentType();		//判断上传文件是否为图片		if (contentType==null||!contentType.startsWith("image/")) {			System.out.println("===不属于图片类型...===");			return;		}
        if (contentType==null||!contentType.startsWith("video")) {
            System.out.println("===不符合上传类型...===");
            return  new Response(500,"资源不符合上传类型");
        }
        try{
            saveUploadFile("/upload/videoFile/",1,resourceVo,fileinfo,request);

        }catch (Exception e){
            log.error(e.getMessage());
            return new Response(500,"视频上传失败");
        }
        return  new Response(0,"资源上传成功");
    }

    private void saveUploadFile(String savePath, Integer resourceType, ResourceVo resourceVo, MultipartFile fileinfo,  HttpServletRequest request) throws Exception{
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
        Subject subject = ShiroUtils.getSubject();
        UserInfo user = (UserInfo) subject.getPrincipal();
        resourceVo.setUId(user.getId());
        resourceVo.setDeptId(user.getSysDept().getId());
        resourceVo.setResourceType(resourceType.toString());
        resourceVo.setCreateTime(LocalDateTime.now());
        resourceVo.setUpdateTime(LocalDateTime.now());
        resourceVo.setUrl(path+"/"+filename);
        resourceinfoService.save(resourceVo);

    }

    @GetMapping("/editVideoResourceList")
    public String editVideoResourceList(){
        return "resource/videoEdit";
    }

    @PostMapping("/videoEditUpload")
    @ResponseBody
    public Response videoEditUpload(String filePath,ResourceVo resourceVo, MultipartFile fileinfo, HttpServletRequest request){
        try{
            if(fileinfo==null){
                resourceVo.setUpdateTime(LocalDateTime.now());
                resourceinfoService.updateById(resourceVo);
            }else {
                if (filePath != null && !"".equals(filePath)) {
                    String path = filePath.substring(0, filePath.lastIndexOf("/"));
                    //System.out.println(path);
                    String realPath = request.getServletContext().getRealPath(path);
                    String filename = fileinfo.getOriginalFilename();
                    filename = UUID.randomUUID().toString()+ filename.substring(filename.lastIndexOf("."));
                    File f= new File(realPath, filename);
                    fileinfo.transferTo(f);
                    resourceVo.setUrl(path+"/"+filename);
                    resourceVo.setUpdateTime(LocalDateTime.now());
                    resourceinfoService.updateById(resourceVo);
                    String oldFilePath=request.getServletContext().getRealPath(filePath);
                    File oldFile=new File(oldFilePath);
                    oldFile.delete();
                }
            }
        }catch(Exception ex){
            log.error(ex.getMessage());
            return new Response(500,"修改错误，系统异常");
        }
        return new Response(0,"资源更新成功");
    }

    @GetMapping("/videoDetailView")
    public String videoDetailView(){
        return "resource/videoView";
    }

    @GetMapping("/audioList")
    public String audioList(){
        return "resource/audioResourceList";
    }

    @GetMapping("/audioResourceAdd")
    public String audioResourceAdd(){
        return "resource/audioAdd";
    }

    @GetMapping("/editAudioResourceList")
    public String editAudioResourceList(){
        return "resource/audioEdit";
    }
    @GetMapping("/audioDetailView")
    public String audioDetailView(){
        return "resource/audioView";
    }
    //audioResourceFileUpload
    @PostMapping("/audioResourceFileUpload")
    @ResponseBody
    public Response audioResourceFileUpload(ResourceVo resourceVo, MultipartFile fileinfo, HttpServletRequest request){
        if(resourceVo!=null){
            if("".equals(resourceVo.getResourceName())||"".equals(resourceVo.getRemark()==null)){
                return  new Response(500,"资源名称或备注为空");
            }
        }
        String contentType = fileinfo.getContentType();		//判断上传文件是否为图片		if (contentType==null||!contentType.startsWith("image/")) {			System.out.println("===不属于图片类型...===");			return;		}
        if (contentType==null||!contentType.startsWith("audio")) {
            System.out.println("===不符合上传类型...===");
            return  new Response(500,"资源不符合上传类型");
        }
        try{
            saveUploadFile("/upload/audioFile/",2,resourceVo,fileinfo,request);

        }catch (Exception e){
            log.error(e.getMessage());
            return new Response(500,"视频上传失败");
        }
        return  new Response(0,"资源上传成功");
    }

    @PostMapping("/audioEditUpload")
    @ResponseBody
    public Response audioEditUpload(String filePath,ResourceVo resourceVo, MultipartFile fileinfo, HttpServletRequest request){
        try{
            if(fileinfo==null){
                resourceVo.setUpdateTime(LocalDateTime.now());
                resourceinfoService.updateById(resourceVo);
            }else {
                if (filePath != null && !"".equals(filePath)) {
                    String path = filePath.substring(0, filePath.lastIndexOf("/"));
                    System.out.println(path);
                    String realPath = request.getServletContext().getRealPath(path);
                    String filename = fileinfo.getOriginalFilename();
                    filename = UUID.randomUUID().toString()+ filename.substring(filename.lastIndexOf("."));
                    File f= new File(realPath, filename);
                    fileinfo.transferTo(f);
                    resourceVo.setUrl(path+"/"+filename);
                    resourceVo.setUpdateTime(LocalDateTime.now());
                    resourceinfoService.updateById(resourceVo);
                    String oldFilePath=request.getServletContext().getRealPath(filePath);
                    File oldFile=new File(oldFilePath);
                    oldFile.delete();
                }
            }
        }catch(Exception ex){
            log.error(ex.getMessage());
            return new Response(500,"修改错误，系统异常");
        }
        return new Response(0,"资源更新成功");
    }

    @GetMapping("/getDeptResourceInfo")
    @ResponseBody
    public List<CheckResourceInfo> getDeptResourceInfo() throws Exception{
        Subject subject = ShiroUtils.getSubject();
        UserInfo user = (UserInfo) subject.getPrincipal();
        ResourceVo resourceVo=new ResourceVo();
        resourceVo.setUserName(user.getUsername());
        try {
            List<DeptResourceInfo> deptResourceList = resourceinfoService.getDeptResourceInfo(resourceVo);
            return changeDeptResourceInfo(deptResourceList);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new Exception("提取数据失败："+e.getMessage());
        }
    }

    private List<CheckResourceInfo> changeDeptResourceInfo(List<DeptResourceInfo> resourceInfos) throws Exception{
        List<Long> deptIdList = resourceInfos.stream().map(DeptResourceInfo::getId).distinct().collect(Collectors.toList());
        List<CheckResourceInfo> parentList=new ArrayList<>();
        for(int i=0;i<deptIdList.size();i++){
            Long deptId=deptIdList.get(i);
            List<DeptResourceInfo> resourceList = resourceInfos.stream().filter(r -> r.getId() == deptId).collect(Collectors.toList());
            if(resourceList.get(0).getRid()==null){
                continue;
            }
            Map<String, List<DeptResourceInfo>> groupInfoMap = resourceList.stream().collect(Collectors.groupingBy(DeptResourceInfo::getResourceTypes));
            CheckResourceInfo checkResourceInfo=new CheckResourceInfo();
            checkResourceInfo.setTitle(resourceList.get(0).getDeptname());
            List<CheckResourceInfo> nodeInfo=new ArrayList<>();
            if(groupInfoMap!=null){
                Set<String> keys = groupInfoMap.keySet();
                if(keys.size()!=0){
                    for(String key:keys){
                        CheckResourceInfo childinfo=new CheckResourceInfo();
                        List<DeptResourceInfo> drInfos = groupInfoMap.get(key);
                        childinfo.setTitle(key);
                        List<CheckResourceInfo> childList=new ArrayList<>();
                        for(DeptResourceInfo info:drInfos){
                            CheckResourceInfo childreninfo=new CheckResourceInfo();
                            childreninfo.setTitle(info.getResourceName());
                            childreninfo.setId(info.getRid());
                            childList.add(childreninfo);
                        }
                        childinfo.setChildren(childList);
                        nodeInfo.add(childinfo);
                    }
                }

            }
            checkResourceInfo.setChildren(nodeInfo);
            parentList.add(checkResourceInfo);
        }
        return parentList;
    }

    @PostMapping("/unlearnedResource")
    @ResponseBody
    public Response unlearnedResource(String code,Long  timestamp){
        try{
            //EncryptUtils解密 {"empID"："","showNum":10,"pageNum":1}
            Integer showNum=10;
            Integer pageNum=1;
            Long empCode=Long.parseLong(code);
            UnLearnResource unLearnResource = employeeService.getUnLearnResourceByPage(pageNum, showNum, empCode);
            return new Response(200, "提取数据成功！",unLearnResource);
        }catch (Exception e){
            log.error(e.getMessage());
            return new Response(500, "提取数据失败！");
        }
    }

    @PostMapping("/learnedResource")
    @ResponseBody
    public Response learnedResource(String code,Long  timestamp){
        try{
            //EncryptUtils解密 {"empID"："","showNum":10,"pageNum":1}
            Integer showNum=10;
            Integer pageNum=1;
            Long empCode=Long.parseLong(code);
            LearnedResource learnedResource = employeeService.getLearnedResourceByPage(pageNum, showNum, empCode);
            return new Response(200, "提取数据成功！",learnedResource);
        }catch (Exception e){
            log.error(e.getMessage());
            return new Response(500, "提取数据失败！");
        }
    }

}
