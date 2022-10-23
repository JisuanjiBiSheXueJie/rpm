package com.rpm.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum CostTypeEnum {

    electricity(1,"电费"),
    water(2,"水费"),
    gasfee(3,"燃气费"),
    pmf(4,"物业费");

    private final Integer value;
    private final String name;

    CostTypeEnum( Integer value,String name) {
        this.name = name;
        this.value = value;
    }
}
