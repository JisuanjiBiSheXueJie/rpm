package com.rpm.config.init;

import com.rpm.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 启动就立即执行
 * 初始化数据
 */
//@Component
public class InitDataConfig implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {

    }
}
