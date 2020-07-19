package cn.unicom.exams.web.config;

import cn.unicom.exams.web.filter.HostFilter;
import cn.unicom.exams.web.filter.XssFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 王长何
 * @create 2020-07-18 23:48
 */
@Configuration
public class HttpHostConfig {

/*    @Bean
    public FilterRegistrationBean httpHostFilterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new HostFilter());
        filterRegistrationBean.setOrder(0);
        filterRegistrationBean.setEnabled(true);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }*/
}
