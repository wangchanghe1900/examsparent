package cn.unicom.exams.web.main;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 王长何
 * @create 2020-01-09 13:53
 */
@SpringBootApplication(scanBasePackages = "cn.unicom.exams")
@MapperScan("cn.unicom.exams.service.mapper")
public class ExamsWebApp {
    public static void main(String[] args) {
        SpringApplication.run(ExamsWebApp.class,args);
    }
}
