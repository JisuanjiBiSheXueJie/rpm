package com.rpm.config.mybatisPlus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.rpm.utils.shiro.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.shiro.SecurityUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

/**
 * 创建时间自动填充配置
 */
@Slf4j
@Configuration
public class MyMetaObjectHandler implements MetaObjectHandler {


    @Override
    public void insertFill(MetaObject metaObject) {

        //自动填充秘钥
        this.setFieldValByName("salt", "pm",metaObject);
        // 添加时自动填充时间
        this.strictInsertFill(metaObject, "createTime", () -> new Date(), Date.class);
        this.strictInsertFill(metaObject, "optTime", () -> new Date(), Date.class);
        this.strictInsertFill(metaObject, "submitTime", () -> new Date(), Date.class);
        this.strictInsertFill(metaObject, "releaseTime", () -> new Date(), Date.class);
        this.strictInsertFill(metaObject, "sendTime", () -> new Date(), Date.class);
        this.strictInsertFill(metaObject, "amountTime", () -> new Date(), Date.class);

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 自动填充修改时间
        this.strictUpdateFill(metaObject, "updateTime", () -> new Date(), Date.class);
        this.strictUpdateFill(metaObject, "delTime", () -> new Date(), Date.class);
        this.strictUpdateFill(metaObject, "freezeTime", () -> new Date(), Date.class);
        this.strictUpdateFill(metaObject, "processingTime", () -> new Date(), Date.class);
    }
}
