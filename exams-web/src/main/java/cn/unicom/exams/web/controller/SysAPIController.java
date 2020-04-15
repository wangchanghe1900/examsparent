package cn.unicom.exams.web.controller;

import cn.unicom.exams.model.entity.SysDept;
import cn.unicom.exams.model.entity.SysEmployee;
import cn.unicom.exams.model.vo.*;
import cn.unicom.exams.model.web.Response;
import cn.unicom.exams.service.service.*;
import cn.unicom.exams.web.utils.MD5Utils;
import cn.unicom.exams.web.utils.ShiroUtils;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.code.kaptcha.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.regex.Pattern;

/**
 * @author 王长何
 * @create 2020-03-31 10:14
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class SysAPIController {
    @Autowired
    private ISysTestpaperService testpaperService;

    @Autowired
    private ISysEmployeeService employeeService;

    @Autowired
    private ISysDeptService deptService;


    @Autowired
    private ISysLearndurationService learndurationService;

    @Autowired
    private ISysTestresultService testresultService;

    @Value("${exams.passNum:60}")
    private Integer passNum;

    @PostMapping("/test/testpaper")
    public Response testpaper(String code, Long timestamp){
        try {
            LocalDateTime localDateTime = Instant.ofEpochMilli(timestamp).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
            log.warn(code+"----testpaper----"+localDateTime.toString());
            ParamsVo paramsVo = JSON.parseObject(code, ParamsVo.class);
            ExamInfo examInfo = testpaperService.getExamInfoByEmpCode(paramsVo.getEmpID(), paramsVo.getExamID(), paramsVo.getShowNum(), paramsVo.getPageNum());
            return new Response(200,"考题提取成功",examInfo);
        } catch (Exception e) {
            log.error("考题提取错误："+e.getMessage());
            return new Response(500,"考题提取失败");
        }
    }

    @PostMapping("/emp/login")
    public Response login(String code,Long timestamp){
        //code="["+code+"]";
        try{
            LocalDateTime localDateTime = Instant.ofEpochMilli(timestamp).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
            log.warn(code+"----login----"+localDateTime.toString());
            EmpInfo empInfo = JSON.parseObject(code, EmpInfo.class);
            if(!StringUtils.isEmpty(empInfo)){
                if(!StringUtils.isEmpty(empInfo.getValidcode())){
                    String kaptcha = ShiroUtils.getKaptcha(Constants.KAPTCHA_SESSION_KEY);
                    if(!kaptcha.equals(empInfo.getValidcode())){
                        return new Response(550,"验证码错误");
                    }
                }
                QueryWrapper<SysEmployee> queryWrapper=new QueryWrapper<>();
                queryWrapper.eq("employeeCode",empInfo.getUserName());
                SysEmployee employee = employeeService.getOne(queryWrapper);
                if(employee == null ){
                    return new Response(400,"用户不存在");
                }
                String pwd= MD5Utils.getAuthenticationInfo(empInfo.getPassword(), employee.getSalt());
                if(!pwd.equals(employee.getPassword())){
                    employee.setLoginFailureTimes(employee.getLoginFailureTimes()+1);
                    employeeService.updateById(employee);
                    return new Response(500,"用户名或密码错误");
                }
                empInfo.setEmpID(Long.parseLong(employee.getEmployeeCode()));
                empInfo.setUserName(employee.getEmployeeCode());
                empInfo.setRealName(employee.getEmployeeName());
                empInfo.setDeptID(employee.getDeptId());
                empInfo.setEmpCode(employee.getEmployeeCode());
                SysDept dept = deptService.getById(employee.getDeptId());
                empInfo.setDeptName(dept.getDeptname());
                empInfo.setUserImg("");
                employee.setLoginFailureTimes(0);
                employee.setLastLoginTime(LocalDateTime.now());
                employeeService.updateById(employee);
                return new Response(200,"登录成功",empInfo);
            }else{
                return new Response(560,"传递参数错误");
            }

        }catch (Exception e){
            log.error("员工登录错误："+e.getMessage());
            return new Response(600,"系统错误");
        }

    }

    @PostMapping("/emp/empTestInfo")
    public Response empTestInfo(String code,Long timestamp){
        //EncryptUtils解密code
        try{
            LocalDateTime localDateTime = Instant.ofEpochMilli(timestamp).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
            log.warn(code+"----empTestInfo----"+localDateTime.toString());
            ParamsVo paramsVo = JSON.parseObject(code, ParamsVo.class);
            EmpTestInfo testInfo = employeeService.getEmpTestInfoByEmpCode(paramsVo.getEmpID());
            return new Response(200, "个人信息反馈成功",testInfo);
        }catch (Exception e){
            log.error("员工学习信息数据提取错误："+e.getMessage());
            return new Response(500, "个人信息反馈失败");
        }

    }

    @PostMapping("resource/unlearnedResource")
    public Response unlearnedResource(String code,Long  timestamp){
        try{
            //EncryptUtils解密 {"empID"："","showNum":10,"pageNum":1}
            LocalDateTime localDateTime = Instant.ofEpochMilli(timestamp).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
            log.warn(code+"----unlearnedResource----"+localDateTime.toString());
            ParamsVo paramsVo = JSON.parseObject(code, ParamsVo.class);
/*            Integer showNum=10;
            Integer pageNum=1;
            Long empCode=Long.parseLong(code);*/
            UnLearnResource unLearnResource = employeeService.getUnLearnResourceByPage(paramsVo.getPageNum(), paramsVo.getShowNum(), paramsVo.getEmpID());
            return new Response(200, "提取数据成功",unLearnResource);
        }catch (Exception e){
            log.error("未学资源数据提取错误："+e.getMessage());
            return new Response(500, "提取数据失败");
        }
    }

    @PostMapping("resource/learnedResource")
    public Response learnedResource(String code,Long  timestamp){
        try{
            //EncryptUtils解密 {"empID"："","showNum":10,"pageNum":1}
            LocalDateTime localDateTime = Instant.ofEpochMilli(timestamp).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
            log.warn(code+"----learnedResource----"+localDateTime.toString());
            ParamsVo paramsVo = JSON.parseObject(code, ParamsVo.class);
/*            Integer showNum=10;
            Integer pageNum=1;
            Long empCode=Long.parseLong(code);*/
            LearnedResource learnedResource = employeeService.getLearnedResourceByPage(paramsVo.getPageNum(), paramsVo.getShowNum(), paramsVo.getEmpID());
            return new Response(200, "提取数据成功",learnedResource);
        }catch (Exception e){
            log.error("已学资源数据提取错误："+e.getMessage());
            return new Response(500, "提取数据失败");
        }
    }

    @PostMapping("resource/learnedResult")
    public Response learnedResult(String code,Long timestamp){
        try {
            LocalDateTime localDateTime = Instant.ofEpochMilli(timestamp).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
            log.warn(code+"----learnedResult----"+localDateTime.toString());
            ParamsVo paramsVo = JSON.parseObject(code, ParamsVo.class);
/*            Long empID=18610810006L;
            Long testID=23L;
            Long resourceID=1L;
            Integer studyDuration=10;*/
            learndurationService.saveLearnInfo(paramsVo.getEmpID(),paramsVo.getExamID(),paramsVo.getResourceID(),paramsVo.getStudyDuration());
            return new Response(200,"数据提交成功");
        } catch (Exception e) {
            log.error("学习资源数据提交错误："+e.getMessage());
            return new Response(500,"数据提交失败");
        }

    }

    @PostMapping("/test/testResult")
    public Response testResult(String code,Long timestamp){
        try {
            LocalDateTime localDateTime = Instant.ofEpochMilli(timestamp).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
            log.warn(code+"----testResult----"+localDateTime.toString());
            code="{\"empID\":\"18610810006\",\"examID\":\"21\",\"totalNum\":5,\"answerNum\":5," +
                    "\"quitTimes\":3,\"duration\":20,\"optionList\":[{\"questNo\":\"2\",\"answer\":\"A\"}," +
                    "{\"questNo\":\"7\",\"answer\":\"A,B,C\"}," +
                    "{\"questNo\":\"9\",\"answer\":\"A,B,C,E\"}," +
                    "{\"questNo\":\"11\",\"answer\":\"A\"}," +
                    "{\"questNo\":\"14\",\"answer\":\"A\"}]}";
            //code="["+code+"]";
            TestResultInfo testResultInfo = JSON.parseObject(code, TestResultInfo.class);
            if(StringUtils.isEmpty(testResultInfo)){
                return new Response(400,"参数错误");
            }
            testResultInfo.setPassNum(passNum);
            ExamScore examScore = testresultService.judgementExaminfo(testResultInfo);
            return new Response(200,"考试结果反馈成功",examScore);
        } catch (Exception e) {
            log.error("考试结果反馈信息错误："+e.getMessage());
            return new Response(500,"考试结果反馈失败");
        }
    }

    @PostMapping("/test/testedinfo")
    public Response testedinfo(String code,Long timestamp){
        try{
            LocalDateTime localDateTime = Instant.ofEpochMilli(timestamp).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
            log.warn(code+"----testedinfo----"+localDateTime.toString());
            ParamsVo paramsVo = JSON.parseObject(code, ParamsVo.class);
            TestedInfo testedInfo = testresultService.gettestedInfoByEmpID(paramsVo.getEmpID(), paramsVo.getShowNum(), paramsVo.getPageNum());
            return new Response(200,"数据提取成功",testedInfo);
        }catch (Exception e){
            log.error("已考统计信息错误："+e.getMessage());
            return new Response(500,"数据提取失败");
        }
    }

    @PostMapping("/emp/resetPwd")
    public Response resetPwd(String code,Long timestamp){
        try{
            LocalDateTime localDateTime = Instant.ofEpochMilli(timestamp).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
            log.warn(code+"----resetPwd----"+localDateTime.toString());
            ParamsVo paramsVo = JSON.parseObject(code, ParamsVo.class);
            String oldPassword=paramsVo.getOldPassword();
            String newPassword=paramsVo.getNewPassword();
            String empID=paramsVo.getEmpID().toString();
            String regex="^^(?![a-zA-z]+$)(?!\\d+$)(?![!@#$%^&*_-]+$)(?![a-zA-z\\d]+$)(?![a-zA-z!@#$%^&*_-]+$)(?![\\d!@#$%^&*_-]+$)[a-zA-Z\\d!@#$%^&*_-]+$";
            if(oldPassword.equals(newPassword)){
                return new Response(400,"新密码和旧密码一样");
            }
            if(StringUtils.isEmpty(oldPassword) || StringUtils.isEmpty(newPassword)){
                return new Response(400,"密码为空");
            }
            if(newPassword.length()<8){
                return new Response(400,"新密码需要8位以上");
            }
            QueryWrapper<SysEmployee> qw=new QueryWrapper<>();
            qw.eq("employeeCode",empID);
            SysEmployee employee = employeeService.getOne(qw);
            if(StringUtils.isEmpty(employee)){
                return new Response(500,"用户ID错误");
            }
            String pwd= MD5Utils.getAuthenticationInfo(oldPassword, employee.getSalt());
            if(!pwd.equals(employee.getPassword())){
                return new Response(500,"旧密码不正确");
            }
            if(!Pattern.matches(regex, newPassword)){
                return new Response(500,"密码复杂度不符合要求");
            }
            String newPwd=MD5Utils.getAuthenticationInfo(newPassword, employee.getSalt());
            employee.setPassword(newPwd);
            employeeService.updateById(employee);
            return new Response(200,"更新成功");
        }catch (Exception e){
            log.error("密码修改错误："+e.getMessage());
            return new Response(500,"更新失败");
        }
    }
}
