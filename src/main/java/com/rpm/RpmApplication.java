package com.rpm;

import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.InternetHeaders;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
@ServletComponentScan
@MapperScan("com.rpm.mapper")
public class RpmApplication {
    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext application = SpringApplication.run(RpmApplication.class, args);

        Environment env = application.getEnvironment();
        String port = env.getProperty("server.port");
        String profiles = env.getProperty("spring.profiles.active");
        //判断是否是开发环境
        if(profiles.equals("dev")){
            String ip = InetAddress.getLocalHost().getHostAddress();
            //启动路径
            System.out.println("\t\thttp://localhost:"+port+"/");
            System.out.println("\t\thttp://"+ip+":"+port+"/");
            //接口文档路径
            System.out.println("\t\thttp://"+ip+":"+port+"/"+"doc.html");
            //druid监控路径
            System.out.println("\t\thttp://"+ip+":"+port+"/"+"druid"+"/"+"login.html");
        }

    }
}
