package com.rpm.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 操作等级枚举
 */
@Getter
public enum LevelEnum {
    /**
     * 枚举类型
     */
    SECUTITY(1,"安全级别"),
    WARNNING(2,"警告级别"),
    DANGERING(3,"危险级别");

    @EnumValue
    private final Integer code;
    @JsonValue
    private final String name;

    LevelEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
