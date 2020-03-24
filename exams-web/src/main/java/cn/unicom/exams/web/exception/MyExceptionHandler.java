package cn.unicom.exams.web.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class MyExceptionHandler {

    @ExceptionHandler
    public String AuthorizationErrorHandler(AuthorizationException e) {
        log.error("没有通过权限验证！");
        return "redirect:/403";
    }

    @ExceptionHandler
    public String ErrorHandler(Exception e) {
        log.error("系统错误："+e.getMessage());
        return "redirect:/list404";
    }
}