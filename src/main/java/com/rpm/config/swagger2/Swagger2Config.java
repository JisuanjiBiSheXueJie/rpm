package com.rpm.config.swagger2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;


/**
 * swagger2 配置类
 */
@Configuration
@EnableWebMvc
@EnableSwagger2
@ComponentScan("com.rpm.controller")
@Slf4j
public class Swagger2Config implements WebMvcConfigurer {

    /**
     * 接口文档管理
     * @return
     */
    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.rpm.controller"))
                .paths(PathSelectors.any())
                .build()
                .groupName("接口文档")
                //增加token验证
                .securitySchemes(security());
    }

    /**
     * 登录接口文档
     * @return
     */
    @Bean
    public Docket login_api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.rpm.controller.LoginController"))
                .paths(PathSelectors.any())
                .build()
                .groupName("登录接口文档")
                //增加token验证
                .securitySchemes(security());
    }

    /**
     * 用户接口文档
     * @return
     */
    @Bean
    public Docket user_api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.rpm.controller.UserController"))
                .paths(PathSelectors.any())
                .build()
                .groupName("用户接口文档")
                //增加token验证
                .securitySchemes(security());
    }

    /**
     * 日志接口文档
     * @return
     */
    @Bean
    public Docket log_api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.rpm.controller.SysLogController"))
                .paths(PathSelectors.any())
                .build()
                .groupName("日志接口文档")
                //增加token验证
                .securitySchemes(security());
    }


    /**
     * 楼栋接口文档
     * @return
     */
    @Bean
    public Docket floors_api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.rpm.controller.FloorsController"))
                .paths(PathSelectors.any())
                .build()
                .groupName("楼栋接口文档")
                //增加token验证
                .securitySchemes(security());
    }

    /**
     * 上传
     * @return
     */
    @Bean
    public Docket upload_api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.rpm.controller.UpLoadFileController"))
                .paths(PathSelectors.any())
                .build()
                .groupName("上传接口文档")
                //增加token验证
                .securitySchemes(security());
    }


    //swagger信息
    public ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                //接口文档标题
                .title("小区物业管理系统接口文档")
                //版本
                .version("v-1")
                //接口文档描述
                .description("用于测试小区物业管理系统的接口数据提交和返回")
                //执照
                .license("The Apache License, Version 2.0")
                //执照地址
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                .build(); //构建接口文档信息
    }


    /**
     *
     * 显示swagger-ui.html文档展示页，还必须注入swagger资源：
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /**
     * 设置全局token
     */
    private List<ApiKey> security(){
        List<ApiKey> list = new ArrayList<>();
        list.add(new ApiKey("Token","Token","header"));
        return list;
    }

    /**
     * 全局上下文
     * @return
     */
    private List<SecurityContext> securityContexts() {
        List<SecurityContext> securityContexts=new ArrayList<>();
        securityContexts.add(
                SecurityContext.builder()
                        .securityReferences(defaultAuth())
                        .forPaths(PathSelectors.regex("^(?!auth).*$"))
                        .build());
        return securityContexts;
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        List<SecurityReference> securityReferences=new ArrayList<>();
        securityReferences.add(new SecurityReference("Authorization", authorizationScopes));
        return securityReferences;
    }
}
