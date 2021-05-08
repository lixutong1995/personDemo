package com.person.config;

import com.alibaba.druid.sql.visitor.functions.Bin;
import com.person.exception.ShipException;
import com.person.pojo.vo.Result;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;


/**
 * @Description 异常集中处理类
 * @Author Xutong Li
 * @Date 2021/5/8
 */
@RestControllerAdvice
public class ShipExceptionHandler {

    /**
     * 全局异常处理器
     * @Author Xutong Li
     * @Date 2021/5/8
     * @param exception       
     * @return com.person.pojo.vo.Result<java.lang.Void>
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handlerBusinessException(Exception exception){
        return Result.error(transferToShipException(exception));
    }

    /**
     * 异常处理器
     * @Author Xutong Li
     * @Date 2021/5/8
     * @param exception
     * @return com.person.exception.ShipException
     */
    private ShipException transferToShipException(Exception exception) {
        ShipException shipException;
        if(exception instanceof ShipException){
            shipException = (ShipException) exception;
        }else if(exception instanceof BindException){
            BindException bindException = (BindException) exception;
            BindingResult bindingResult = bindException.getBindingResult();
            shipException = new ShipException(getErrorMsg(bindingResult));
        }else if(exception instanceof MethodArgumentNotValidException){
            MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) exception;
            BindingResult bindingResult = methodArgumentNotValidException.getBindingResult();
            shipException = new ShipException(getErrorMsg(bindingResult));
        }else{
            shipException = new ShipException(exception.getMessage());
        }
        return shipException;
    }

    /**
     * 获取异常提示信息
     * @Author Xutong Li
     * @Date 2021/5/8
     * @param bindingResult
     * @return java.lang.String
     */
    private String getErrorMsg(BindingResult bindingResult) {
        List<FieldError> fieldErrorList = bindingResult.getFieldErrors();
        StringBuilder stringBuilder = new StringBuilder(64);
        fieldErrorList.forEach(fieldError -> {
            stringBuilder.append(fieldError.getDefaultMessage());
            stringBuilder.append("-");
        });
        return stringBuilder.toString();
    }
}
