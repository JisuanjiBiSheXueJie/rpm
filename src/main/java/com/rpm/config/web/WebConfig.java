package com.rpm.config.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 头像路径设置
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${imgSetting.path}")
    private String imgPath;

    //文件虚拟路径
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/api/file/**").addResourceLocations("file:" + imgPath + "/");
    }
}
