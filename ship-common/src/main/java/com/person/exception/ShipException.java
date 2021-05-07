package com.person.exception;

import com.person.constants.ShipExceptionEnum;

/**
 * @Description 公共异常类
 * @Author Xutong Li
 * @Date 2021/3/5
 */
public class ShipException extends RuntimeException{

    private Integer code;

    private String errMsg;

    public ShipException(ShipExceptionEnum exceptionEnum){
        super(exceptionEnum.getMsg());
        this.code = exceptionEnum.getCode();
        this.errMsg = exceptionEnum.getMsg();
    }

    public ShipException(String errMsg){
        super(errMsg);
        this.errMsg = errMsg;
        this.code = 5000;
    }

    public ShipException(Integer code, String errMsg){
        this.code = code;
        this.errMsg = errMsg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
