package cn.unicom.exams.web.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
@WebFilter(filterName = "otherFilter")
@Slf4j
public class HostFilter implements Filter {
 
  /**
   * 自定义实现host白名单添加
   */
  @Value("${exams.ALLOWED_SERVERNAMES}")
  private String ALLOWED_SERVERNAMES;
 
  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
//    System.out.println("Filter初始化中");
  }
 
  /**
   * host拦截
   */
  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                       FilterChain filterChain) throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    HttpServletResponse response = (HttpServletResponse) servletResponse;
//    String host = request.getHeader("host");
    String serverName = request.getServerName();
    //System.out.println("serverName-debug:" + serverName);
    if (!isEmpty(serverName)) {
      if (checkBlankList(serverName)) {
        filterChain.doFilter(servletRequest, servletResponse);
      } else {
        log.error("[serverName deny access tips]->" + serverName);
        response.setStatus(403);
        response.flushBuffer();
      }
    } else {
      filterChain.doFilter(servletRequest, servletResponse);
    }
 
  }
 
  @Override
  public void destroy() {
//    System.out.println("Filter销毁");
  }
 
  /**
   * 校验当前host是否在白名单中
   */
  private boolean checkBlankList(String serverName) {
    String[] allowdServerName = ALLOWED_SERVERNAMES.split(",");
    List<String> serverNameList = Arrays.asList(allowdServerName);
    //log.warn("访问地址："+serverName);
    for(String str : serverNameList){
      //String whiteAdd=serverName.substring(0,serverName.lastIndexOf("."));
      if(!isEmpty(str) && str.equals(serverName)){
        return true;
      }
    }
    return false;
  }
 
  /**
   * 判空
   */
  public boolean isEmpty(Object str) {
    return str == null || "".equals(str);
  }
 
}