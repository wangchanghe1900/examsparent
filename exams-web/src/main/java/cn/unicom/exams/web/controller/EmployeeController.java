package cn.unicom.exams.web.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.unicom.exams.model.entity.SysEmployee;
import cn.unicom.exams.model.vo.ButtonInfo;
import cn.unicom.exams.model.vo.EmployeeInfo;
import cn.unicom.exams.model.vo.EmployeeVo;
import cn.unicom.exams.model.web.Response;
import cn.unicom.exams.model.web.WebResponse;
import cn.unicom.exams.service.service.ISysDeptService;
import cn.unicom.exams.service.service.ISysEmployeeService;
import cn.unicom.exams.web.utils.ButtonAuthorUtils;
import cn.unicom.exams.web.utils.MD5Utils;
import cn.unicom.exams.web.utils.SecurityCode;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.code.kaptcha.Producer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author 王长何
 * @create 2020-01-22 15:52
 */
@Controller
@RequestMapping("/emp")
@Slf4j
public class EmployeeController {
    @Autowired
    private ISysEmployeeService employeeService;

    @Autowired
    private ButtonAuthorUtils buttonAuthorUtils;

    @Autowired
    private Producer producer;

    @Autowired
    private ISysDeptService deptService;


    @GetMapping("/empList")
    @RequiresPermissions("emp:list")
    public String empList(){
        return "employee/empList";
    }

    @GetMapping("/getEmpList")
    @RequiresPermissions("emp:find")
    @ResponseBody
    public WebResponse getEmpList(int page, int limit, String data){
        EmployeeVo employeeVo=new EmployeeVo();
        if(data!=null){
            data="["+data+"]";
            List<EmployeeVo> employeeVos = JSON.parseArray(data, EmployeeVo.class);
            employeeVo=employeeVos.get(0);
        }
/*        Subject subject = ShiroUtils.getSubject();
        UserInfo user = (UserInfo) subject.getPrincipal();
        if(!"admin".equals(user.getUsername())){
            employeeVo.setDeptId(user.getDeptId());
        }*/
        try{
            IPage<EmployeeInfo> employeeInfoByPage = employeeService.getEmployeeInfoByPage(page, limit, employeeVo);
            ButtonInfo emppower = buttonAuthorUtils.getButtonAuthority("emp");
            Long empcount=employeeInfoByPage.getTotal();
            Integer record=employeeInfoByPage.getRecords().size();
            for (int i = 0; i < record; i++) {
                employeeInfoByPage.getRecords().get(i).setIsDel(emppower.getIsDel());
                employeeInfoByPage.getRecords().get(i).setIsDetail(emppower.getIsDetail());
                employeeInfoByPage.getRecords().get(i).setIsEdit(emppower.getIsEdit());
            }
            return new WebResponse(0,"提取员工信息成功！",empcount.intValue(),employeeInfoByPage.getRecords());
        }catch (Exception e){
            log.error(e.getMessage());
            return new WebResponse(500,"提取员工信息错误！",0);
        }

    }

    @GetMapping("/delEmployeeById")
    @RequiresPermissions("emp:delete")
    @ResponseBody
    public Response delEmployeeById(Long id){
        try{
            //employeeService.removeById(id);
            List<Long> ids=new ArrayList<>();
            ids.add(id);
            employeeService.deleteEmpInfo(ids);
            return  new Response(200,"删除信息成功！");
        }catch (Exception e){
            log.error(e.getMessage());
            return new Response(500,"删除信息失败！");
        }
    }

    @PostMapping("/delEmployeeByIds")
    @RequiresPermissions("emp:batchdel")
    @ResponseBody
    public Response delEmployeeByIds(String ids){
        try{
            String[] idArr=ids.split(",");
            List<Long> idList=new ArrayList<>();
            for(String id : idArr){
                idList.add(Long.parseLong(id));
            }
            //employeeService.removeByIds(idList);
            employeeService.deleteEmpInfo(idList);
            return  new Response(200,"删除信息成功！");
        }catch (Exception e){
            log.error(e.getMessage());
            return new Response(500,"删除信息失败！");
        }
    }
    @GetMapping("/addEmpList")
    @RequiresPermissions("emp:add")
    public String addEmpList(){
        return "employee/empAdd";
    }

    @PostMapping("/saveEmployeeInfo")
    @ResponseBody
    public Response saveEmployeeInfo(String employeeInfo){
        try{
            if(employeeInfo!=null && employeeInfo.length()>5){
                employeeInfo="["+employeeInfo+"]";
                List<EmployeeVo> employeeVos = JSON.parseArray(employeeInfo, EmployeeVo.class);
                EmployeeVo employeeVo = employeeVos.get(0);
                if(StringUtils.isBlank(employeeVo.getEmployeeCode()) || StringUtils.isEmpty(employeeVo.getEmployeeCode())){
                    return  new Response(500,"员工工号不能为空！");
                }
                if(employeeVo.getId()!=null){
                    employeeVo.setUpdateTime(LocalDateTime.now());
                    //employeeService.updateById(employeeVo);
                    employeeService.saveEmpInfo(employeeVo);
                }else{
                    //employeeVo.setEmployeeCode(employeeVo.getMobile().toString());
                    employeeVo.setCreateTime(LocalDateTime.now());
                    employeeVo.setUpdateTime(LocalDateTime.now());
                    employeeVo.setLoginFailureTimes(0);
                    String salt = SecurityCode.getSecurityCode();
                    String pwd = MD5Utils.getAuthenticationInfo("Abcd#123!", salt);//初始密码：Abcd#123!
                    employeeVo.setSalt(salt);
                    employeeVo.setPassword(pwd);
                    //employeeService.save(employeeVo);
                    employeeService.saveEmpInfo(employeeVo);
                }

                return  new Response(200,"保存员工信息成功！");
            }else{
                return  new Response(500,"员工信息为空！");
            }

        }catch (Exception e){
            log.error(e.getMessage());
            return new Response(500,"保存员工信息失败！");
        }
    }
    @GetMapping("/editempList")
    @RequiresPermissions("emp:edit")
    public String editempList(){
        return "employee/empAdd";
    }

    @GetMapping("/showDetailList")
    @RequiresPermissions("emp:detail")
    public String showDetailList(){
        return "employee/empDetail";
    }

    @GetMapping("/importEmpList")
    @RequiresPermissions("emp:import")
    public String importEmpList(){
        return "employee/empImport";
    }

    @PostMapping("/importEmpExcel")
    @ResponseBody
    public Response importEmpExcel(MultipartFile excelinfo, HttpServletRequest request){
        try {
            String contentType = excelinfo.getContentType();
            if (contentType == null || !(contentType.startsWith("application/vnd.ms-excel") || contentType.startsWith("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))) {
                //System.out.println("===不符合上传类型...===");
                return new Response(500, "导入文件不符合上传类型");
            }
            ExcelReader reader = ExcelUtil.getReader(excelinfo.getInputStream());
            List<Map<String, Object>> maps = reader.readAll();
            Integer count=employeeService.importEmpInfo(maps);
            return new Response(200,"导入员工信息成功,共导入"+count+"条",count);
        }catch (Exception e){
           log.error("导入员工信息出错："+e.getMessage());
           return new Response(500,"导入员工信息错误");
        }
    }


    @PostMapping("/resetPwdEmpByIds")
    @RequiresPermissions("emp:resetPwd")
    @ResponseBody
    public Response resetPwdEmpByIds(String ids){
        try{
            String[] idArr=ids.split(",");
            List<Long> empIds=new ArrayList<>();
            for(String id:idArr){
                empIds.add(Long.parseLong(id));
            }
            Collection<SysEmployee> sysEmployees = employeeService.listByIds(empIds);
            for(SysEmployee employee:sysEmployees){
                String salt = employee.getSalt();
                String pwd = MD5Utils.getAuthenticationInfo("Abcd#123!", salt);
                employee.setPassword(pwd);
                employeeService.updateById(employee);
            }
            return new Response(200, "初始化密码成功,初始密码为:Abcd#123!");
        }catch (Exception e){
            log.error(e.getMessage());
            return new Response(500, "初始化密码失败！");
        }
    }

    @GetMapping("/getEmployeeCount")
    @ResponseBody
    public Response getEmployeeCount(){
        try{
            int count = employeeService.count();
            return new Response(200, "",count);
        }catch (Exception e){
            log.error("提取员工数量失败："+e.getMessage());
            return new Response(500, "提取员工数量失败！");
        }
    }



}
