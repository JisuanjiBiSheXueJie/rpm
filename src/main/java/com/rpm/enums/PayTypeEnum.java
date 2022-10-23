package com.rpm.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 支付类型
 */
@Getter
public enum PayTypeEnum {

    XJ(0,"现金"),
    WX(1,"微信"),
    ZFB(2,"支付宝");

    @EnumValue //标记数据库存的值是value
    private final Integer value;

    @JsonValue //序列化枚举名
    private final String name;

    PayTypeEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
