package com.person.pojo.vo;

import com.person.exception.ShipException;

import java.io.Serializable;

/**
 * @Description 响应结果实体类
 * @Author Xutong Li
 * @Date 2021/5/1
 */
public class Result<T> implements Serializable {

    /**
     * 响应状态码
     */
    private int code;

    /**
     * 响应信息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    private Result(){
    }

    /**
     * @Description 返回成功信息
     * @Author Xutong Li
     * @Date 2021/5/1
     * @param
     * @return com.person.pojo.vo.Result<T>
     */
    public static <T> Result<T> success(T data){
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("success");
        result.setData(data);
        return result;
    }

    /**
     * @Description 响应成功信息且没有相应数据
     * @Author Xutong Li
     * @Date 2021/5/1
     * @param
     * @return com.person.pojo.vo.Result<T>
     */
    public static <T> Result<T> success(){
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("success");
        return result;
    }

    /**
     * @Description 响应失败信息
     * @Author Xutong Li
     * @Date 2021/5/1
     * @return com.person.pojo.vo.Result<T>
     */
    public static <T> Result<T> fail(){
        Result<T> result = new Result<>();
        result.setCode(500);
        result.setMessage("fail");
        return result;
    }

    /**
     * @Description 响应错误信息
     * @Author Xutong Li
     * @Date 2021/5/1
     * @param shipException
     * @return com.person.pojo.vo.Result<T>
     */
    public static <T> Result<T> error(ShipException shipException){
        Result<T> result = new Result<>();
        result.setCode(shipException.getCode());
        result.setMessage(shipException.getErrMsg());
        return result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
