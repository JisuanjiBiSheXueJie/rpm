package com.rpm.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 登录枚举
 */
@Getter
public enum LoginEnum {
    LOGIN("登录",1),LOGOUT("登出",2);

    @JsonValue //序列化枚举名
    private final String name;
    @EnumValue //标记数据库存的值是code
    private final Integer value;


    LoginEnum(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
