package cn.unicom.exams.service.configuration;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author 王长河
 * @create 2019-11-18 16:06
 */
@Configuration
@ConditionalOnClass(value= DruidDataSource.class)
public class DruidDataSourceConfig {

    @Bean
    @ConfigurationProperties(prefix="spring.datasource")
    @ConditionalOnMissingBean
    public DataSource createDruidDataSource(){
        return new DruidDataSource();
    }
}
