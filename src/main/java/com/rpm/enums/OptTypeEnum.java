package com.rpm.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 操作类型枚举
 */
@Getter
public enum OptTypeEnum {

    /**
     * 操作类型
     */
    UNKNOWN(0,"未知操作"),
    SELECT(1,"查询"),
    INSERT(2,"插入"),
    UPDATE(3,"更新"),
    DELETE(4,"删除"),
    EXPORT(5,"导出");

    @EnumValue
    private final Integer code;
    @JsonValue
    private final String name;


    OptTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
