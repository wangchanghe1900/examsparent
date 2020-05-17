package cn.unicom.exams.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@ControllerAdvice
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Value("${exams.uploadPath}")
    private String uploadPath;
    @Bean
    public MyHandleIntercept myHandleIntercept(){
        return new MyHandleIntercept();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //静态资源放行
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler( "/upload/**").addResourceLocations("file:"+uploadPath);
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(myHandleIntercept());
    }

}