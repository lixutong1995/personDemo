package com.person.Utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @Description 请求工具类
 * @Author Xutong Li
 * @Date 2021/5/7
 */
public class RequestUtils {

    private RequestUtils(){

    }

    /**
     * 获取当前的HttpServletRequest
     * @Author Xutong Li
     * @Date 2021/5/7
     * @param        
     * @return javax.servlet.http.HttpServletRequest
     */
    public static HttpServletRequest getRequst(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return Objects.isNull(attributes) ? null : attributes.getRequest();
    }
}
