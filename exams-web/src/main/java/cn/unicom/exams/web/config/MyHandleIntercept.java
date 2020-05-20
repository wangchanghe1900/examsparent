package cn.unicom.exams.web.config;

import cn.unicom.exams.model.entity.SysSystemlog;
import cn.unicom.exams.model.vo.UserInfo;
import cn.unicom.exams.service.service.ISysSystemlogService;
import cn.unicom.exams.web.utils.ShiroUtils;
import com.alibaba.druid.sql.visitor.ExportParameterizedOutputVisitor;
import com.alibaba.fastjson.JSON;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author 王长何
 * @create 2020-04-20 16:45
 */
@Component
public class MyHandleIntercept implements HandlerInterceptor {
    @Autowired
    private ISysSystemlogService sysSystemlogService;
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        String requestURL = request.getRequestURI();
        //System.out.println("requestURL = " + requestURL);
        if(requestURL.contains("save") || requestURL.contains("edit") || requestURL.contains("del") || requestURL.contains("resetPwd")){
            Subject subject = ShiroUtils.getSubject();
            UserInfo user = (UserInfo) subject.getPrincipal();
            SysSystemlog sysSystemlog=new SysSystemlog();
            sysSystemlog.setRequestURL(request.getServletPath());
            sysSystemlog.setUserCode(user.getUsername());
            sysSystemlog.setRequestAddress(request.getRemoteAddr());
            if(requestURL.contains("save")){
                sysSystemlog.setOperatorType("新增");
            }else if(requestURL.contains("edit")){
                sysSystemlog.setOperatorType("编辑");
            }else if(requestURL.contains("del")){
                sysSystemlog.setOperatorType("删除");
            }else if(requestURL.contains("resetPwd")){
                sysSystemlog.setOperatorType("重置密码");
            }
            Map<String,String[]> map = request.getParameterMap();
            String content=JSON.toJSONString(map);
            content=content.length()>2000?content.substring(0,2000):content;
            sysSystemlog.setContent(content);
            sysSystemlog.setOperatorDateTime(LocalDateTime.now());
            sysSystemlogService.save(sysSystemlog);
        }


    }
}
