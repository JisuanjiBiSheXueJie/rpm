package com.rpm.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * 通用枚举类型
 */
public class PmEnum {

    /**
     * 是否删除通过枚举
     */
    public enum isDel{
        ISDEL(1,"已删除"),
        NODEL(0,"未删除");

        private final Integer code;
        private final String name;

        public Integer getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        isDel(Integer code, String name) {
            this.code = code;
            this.name = name;
        }
    }


    /**
     * 用户状态枚举
     */

    public enum StatusEnum {
        NORMAL(0,"正常"),FREEZE(1,"冻结");

        private final Integer code;
        private final String name;

        public Integer getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        StatusEnum(Integer code, String name) {
            this.code = code;
            this.name = name;
        }
    }

    /**
     * 是否通用枚举
     */
    public enum YES_NO{
        YES(1,"是"),NO(0,"否");
        private final Integer code;
        private final String name;

        public Integer getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        YES_NO(Integer code, String name) {
            this.code = code;
            this.name = name;
        }
    }


}
