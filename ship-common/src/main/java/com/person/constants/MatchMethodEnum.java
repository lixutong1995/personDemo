package com.person.constants;

/**
 * @Description 匹配方式枚举类
 * @Author Xutong Li
 * @Date 2021/5/7
 */
public enum MatchMethodEnum {

    EQUAL((byte)1, "="),

    REGEX((byte)2, "regex"),

    LIKE((byte)3,"like");

    private Byte code;

    private String desc;

    MatchMethodEnum(Byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Byte getCode() {
        return code;
    }

    public void setCode(Byte code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
