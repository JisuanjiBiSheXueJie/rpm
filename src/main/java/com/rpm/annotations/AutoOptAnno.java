package com.rpm.annotations;

import com.rpm.enums.LevelEnum;
import com.rpm.enums.OptTypeEnum;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoOptAnno {
    //描述
    String detail();

    //操作等级
    LevelEnum levelEnum() default LevelEnum.SECUTITY;

    //操作类型
    OptTypeEnum optTypeEnum();
}
