package cn.unicom.exams.web.controller;

import cn.unicom.exams.model.entity.SysLoginlog;
import cn.unicom.exams.model.entity.SysSystemlog;
import cn.unicom.exams.model.web.WebResponse;
import cn.unicom.exams.service.service.ISysLoginlogService;
import cn.unicom.exams.service.service.ISysSystemlogService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 王长何
 * @create 2020-04-17 15:36
 */
@Controller
@RequestMapping("/log")
@Slf4j
public class SystemLogController {
    @Autowired
    private ISysLoginlogService sysLoginlogService;

    @Autowired
    private ISysSystemlogService sysSystemlogService;

    @GetMapping("/loginlogList")
    public String loginlogList(){
        return "log/loginlog";
    }

    @GetMapping("/getLoginLogList")
    @ResponseBody
    public WebResponse getLoginLogList(int page,int limit,SysLoginlog sysLoginlog){
        try{
            Page<SysLoginlog> pg=new Page<>(page,limit);
            QueryWrapper<SysLoginlog> queryWrapper=new QueryWrapper<>(sysLoginlog);
            //queryWrapper.likeRight(StringUtils.isNotEmpty(userCode),"userCode",userCode);
            queryWrapper.orderByDesc("loginDateTime");
            IPage<SysLoginlog> loginPage = sysLoginlogService.page(pg, queryWrapper);
            Long total = loginPage.getTotal();
            return new WebResponse(0,"",total.intValue(),loginPage.getRecords());
        }catch (Exception e){
            log.error("提取登录日志失败："+e.getMessage());
            return new WebResponse(500,"提取登录日志失败",0);
        }
    }

    @GetMapping("/systemlogList")
    public String systemlogList(){
        return "log/systemlog";
    }

    @GetMapping("/getSystemLogList")
    @ResponseBody
    public WebResponse getSystemLogList(int page, int limit, SysSystemlog sysSystemlog){
        try{
            Page<SysSystemlog> pg=new Page<>(page,limit);
            QueryWrapper<SysSystemlog> queryWrapper=new QueryWrapper<>(sysSystemlog);
            queryWrapper.orderByDesc("operatorDateTime");
            IPage<SysSystemlog> systemlogIPage = sysSystemlogService.page(pg, queryWrapper);
            Long total = systemlogIPage.getTotal();
            return new WebResponse(0,"",total.intValue(),systemlogIPage.getRecords());
        }catch (Exception e){
            log.error("提取系统日志失败："+e.getMessage());
            return new WebResponse(500,"提取系统日志失败",0);
        }
    }
}
