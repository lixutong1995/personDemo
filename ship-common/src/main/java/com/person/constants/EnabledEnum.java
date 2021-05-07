package com.person.constants;

/**
 * @Description 是否启用枚举类
 * @Author Xutong Li
 * @Date 2021/3/9
 */
public enum EnabledEnum {
    NOT_ENABLE((byte)0, "未启用"),

    ENABLE((byte)1, "启用");

    private Byte code;

    private String desc;

    EnabledEnum(Byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Byte getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
