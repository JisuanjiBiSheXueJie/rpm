package com.rpm.annotations;

/**
 * 登录注解
 */

import com.rpm.enums.LoginEnum;
import com.rpm.enums.PmEnum;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoLoginAnno {

    //操作
    String value();

    //用户行为
    LoginEnum loginEnum();
}
