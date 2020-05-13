package cn.unicom.exams.web.controller;

import cn.unicom.exams.model.vo.*;
import cn.unicom.exams.model.web.Response;
import cn.unicom.exams.model.web.WebResponse;
import cn.unicom.exams.service.service.*;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 王长何
 * @create 2020-04-09 10:10
 */
@Controller
@RequestMapping("/statis")
@Slf4j
public class StatisticsController {
    @Autowired
    private ISysTeststatisticsService teststatisticsService;

    @Autowired
    private ISysUnlearndurationService unlearndurationService;

    @Autowired
    private ISysLearndurationService learndurationService;

    @Autowired
    private ISysTestresultService testresultService;

    @Autowired
    private ISysDeptdurationService deptdurationService;

    @Autowired
    private ISysDeptreturnService deptreturnService;

    @Autowired
    private ISysTestquestionsService testquestionsService;

    @GetMapping("teststatisList")
    @RequiresPermissions("teststatis:list")
    public String teststatisList(){
        return "statis/teststatistics";
    }


    @GetMapping("/getTestStatisList")
    @RequiresPermissions("teststatis:find")
    @ResponseBody
    public WebResponse getTestStatisList(int page, int limit, TestStatisticsVo testStatisticsVo){
        try{
            IPage<TestStatisticsInfo> testStatisticsInfoByPage = teststatisticsService.getTestStatisticsInfoByPage(page, limit, testStatisticsVo);
            Long total=testStatisticsInfoByPage.getTotal();
            return new WebResponse(0,"",total.intValue(),testStatisticsInfoByPage.getRecords());
        }catch (Exception e){
            log.error("提取试卷统计错误:"+e.getMessage());
            return new WebResponse(500,"提取试卷统计错误",0);
        }
    }

    @GetMapping("/untestList")
    @RequiresPermissions("untest:list")
    public String untestList(){
        return "/statis/untest";
    }

    @GetMapping("/getUntestList")
    @RequiresPermissions("untest:find")
    @ResponseBody
    public WebResponse getUntestList(int page, int limit, String  data){
        try {
            UntestVo untestVo= JSON.parseObject(data,UntestVo.class);
            IPage<UnTestInfo> unTestInfoByPage = teststatisticsService.getUnTestInfoByPage(page, limit, untestVo);
            Long total=unTestInfoByPage.getTotal();
            return new WebResponse(0,"",total.intValue(),unTestInfoByPage.getRecords());
        }catch (Exception e){
            log.error("提取未考信息失败:"+e.getMessage());
            return new WebResponse(500,"提取未考信息失败",0);
        }
    }

    @GetMapping("emplearntimesList")
    @RequiresPermissions("emplearned:list")
    public String emplearntimesList(){
        return "statis/statisresource";
    }

    @GetMapping("/getUnlearnResourceList")
    @RequiresPermissions("emplearned:find")
    @ResponseBody
    public WebResponse getUnlearnResourceList(int page, int limit,Long deptId){
        try{
            IPage<UnlearnEmpInfo> unlearnResEmpInfoByPage = unlearndurationService.getUnlearnResEmpInfoByPage(page, limit, deptId);
            Long total = unlearnResEmpInfoByPage.getTotal();
            return new WebResponse(0,"",total.intValue(),unlearnResEmpInfoByPage.getRecords());
        }catch (Exception e){
            log.error("提取未学资源错误："+e.getMessage());
            return new WebResponse(500,"提取未学资源错误",0);
        }
    }

    @GetMapping("/getLearnResourceList")
    @RequiresPermissions("emplearned:find")
    @ResponseBody
    public WebResponse getLearnResourceList(int page,int limit,Long deptId){
        try{
            IPage<LearnedResourceEmpInfo> learnedResEmpByPage = learndurationService.getLearnedResEmpByPage(page, limit, deptId);
            Long total = learnedResEmpByPage.getTotal();
            return new WebResponse(0,"",total.intValue(),learnedResEmpByPage.getRecords());
        }catch (Exception e){
            log.error("提取已学资源错误："+e.getMessage());
            return new WebResponse(500,"提取已学资源错误",0);
        }
    }

    @GetMapping("/getEmpTestInfoList")
    @RequiresPermissions("emplearned:find")
    @ResponseBody
    public WebResponse getEmpTestInfoList(int page,int limit,Long deptId){
        try{
            IPage<EmpTestResultInfo> empTestResultInfoByPage = testresultService.getEmpTestResultInfoByPage(page, limit, deptId);
            Long total = empTestResultInfoByPage.getTotal();
            return new WebResponse(0,"",total.intValue(),empTestResultInfoByPage.getRecords());
        }catch (Exception e){
            log.error("提取员工考试信息错误："+e.getMessage());
            return new WebResponse(500,"提取员工考试信息错误",0);
        }
    }

    @GetMapping("/deptstatisList")
    @RequiresPermissions("deptstatis:list")
    public String deptstatisList(){
        return "statis/deptstatistest";
    }

    @GetMapping("/getDeptResourceList")
    @RequiresPermissions("deptstatis:find")
    @ResponseBody
    public WebResponse getDeptResourceList(int page,int limit,Long deptId){
        try{
            IPage<DeptResourceStatisInfo> deptResourceStatisInfoIPage= deptdurationService.getDeptResourceStatisInfo(page,limit,deptId);
            Long total = deptResourceStatisInfoIPage.getTotal();
            return new WebResponse(0,"",total.intValue(),deptResourceStatisInfoIPage.getRecords());
        }catch (Exception e){
            log.error("提取部门学习信息错误："+e.getMessage());
            return new WebResponse(500,"提取部门学习信息错误",0);
        }
    }

    @GetMapping("/getDeptTestReturnList")
    @RequiresPermissions("deptstatis:find")
    @ResponseBody
    public WebResponse getDeptTestReturnList(int page,int limit,Long deptId){
        try{
            IPage<DeptTestStatisInfo> deptTestStatisInfoIPage = deptreturnService.getDeptTestStatisInfo(page,limit,deptId);
            Long  total = deptTestStatisInfoIPage.getTotal();
            return new WebResponse(0,"",total.intValue(),deptTestStatisInfoIPage.getRecords());
        }catch (Exception e){
            log.error("提取部门考试返回信息错误："+e.getMessage());
            return new WebResponse(500,"提取部门考试返回信息错误",0);
        }
    }

    @GetMapping("/getAnswerCount")
    @ResponseBody
    public Response getAnswerCount(){
        try{
            int count = testresultService.count();
            return new Response(200,"",count);
        }catch (Exception e){
            log.error("提取系统答题数量错误："+e.getMessage());
            return  new Response(500,"提取系统答题数量错误");
        }
    }

    @GetMapping("/getSevenTestResultCount")
    @ResponseBody
    public List<Integer> getSevenTestResultCount(){
        try{
            List<Integer> sevenTestResultCount = testresultService.getSevenTestResultCount();
            return sevenTestResultCount;
        }catch (Exception e){
            log.error("提取七天内考试人数错误："+e.getMessage());
            //return  new Response(500,"提取系统考卷数量错误");
        }
        return null;
    }

    @GetMapping("/getSevenDate")
    @ResponseBody
    public List<String> getSevenDate(){
        try{
            List<String> dateList=new ArrayList<>();
            LocalDate now = LocalDate.now();
            for(int i=7;i>0;i--){
                dateList.add(now.plusDays(-i).toString());
            }
            //System.out.println(now.plusDays(-1));
            //List<Integer> sevenTestResultCount = testresultService.getSevenTestResultCount();
            return dateList;
        }catch (Exception e){
            log.error("提取七天内日期："+e.getMessage());
            return null;
        }

    }

    @GetMapping("/getSevenLearnCount")
    @ResponseBody
    public List<Integer> getSevenLearnCount(){
        try{
           return learndurationService.getSenvenLearnResourceCount();
        }catch (Exception e){
            log.error("提取七天内学习次数错误："+e.getMessage());
            return null;
        }
    }

    @GetMapping("/getSevenAnswerCount")
    @ResponseBody
    public List<Integer> getSevenAnswerCount(){
        try{
            return testquestionsService.getSevenAnswerCount();
        }catch (Exception e){
            log.error("提取七天内答题数量错误："+e.getMessage());
            return null;
        }
    }
}
