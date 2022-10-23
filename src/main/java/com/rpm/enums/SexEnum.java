package com.rpm.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 性别枚举
 */
@Getter
public enum  SexEnum {

    MAN(1,"男"),WOMAN(0,"女");

    @EnumValue
    private final Integer code;
    @JsonValue
    private final String name;


    SexEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
