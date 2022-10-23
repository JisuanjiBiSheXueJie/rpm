package com.rpm.config.druid;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * druid配置类
 */
@Configuration
public class DruidConfig {

    /**
     * 读取配置文件
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource getDataSource(){
        return new DruidDataSource();
    }


    /**
     * 数据源监控
     * @return
     */
    @Bean
    public ServletRegistrationBean sateViewServlet(){
        ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        Map<String, Object> map = new HashMap<>();

        //登录名
        map.put("loginUsername","admin");
        map.put("loginPassword","admin");
        //默认是允许所有的访问
        map.put("allow","");
        // IP黑名单 (存在共同时，deny优先于allow)
        map.put("deny","");

        bean.setInitParameters(map);
        return bean;
    }

    /**
     * 过滤器
     * @return
     */
    @Bean
    public FilterRegistrationBean webStaFilter(){
        FilterRegistrationBean bean = new FilterRegistrationBean<>();
        bean.setFilter(new WebStatFilter());

        Map<String, Object> map = new HashMap<>();
        map.put("exclusions","*.js,*.css,/druid/*");
        bean.setInitParameters(map);
        bean.setUrlPatterns(Arrays.asList("/*"));

        return bean;
    }







}
