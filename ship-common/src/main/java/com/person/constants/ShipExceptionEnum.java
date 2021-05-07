package com.person.constants;

/**
 * @Description 异常处理枚举
 * @Author Xutong Li
 * @Date 2021/3/5
 */
public enum ShipExceptionEnum {

    /**
     * 参数错误
     */
    PARAM_ERROR(1000, "参数错误"),

    /**
     * 服务没有找到
     */
    SERVICE_NOT_FIND(1001, "服务没有找到，是否未注册"),

    /**
     * 无效的配置
     */
    CONFIG_ERROR(1002,"无效的配置"),

    /**
     * 用户名或密码错误
     */
    LOGIN_ERROR(1003, "用户名或密码错误"),

    /**
     * 当前没有登录
     */
    NOT_LOGIN(1004, "当前没有登录"),

    /**
     * token失效
     */
    TOKEN_ERROR(1005, "token失效");


    private Integer code;

    private String msg;

    ShipExceptionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
