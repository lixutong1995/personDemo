package com.person.interceptor;

import com.person.Utils.JwtUtils;
import com.person.constants.AdminConstants;
import com.person.constants.ShipExceptionEnum;
import com.person.exception.ShipException;
import com.person.pojo.PayLoad;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @Description 登录拦截器
 * @Author Xutong Li
 * @Date 2021/5/8
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String token = null;
        Cookie[] cookies = request.getCookies();
        if(Objects.isNull(cookies)){
            response.sendRedirect("/user/login/page");
            return false;
        }
        for(Cookie cookie : cookies){
            if(cookie.getName().equals(AdminConstants.TOKEN_NAME)){
                token = cookie.getValue();
            }
        }
        if(StringUtils.isEmpty(token)){
            response.sendRedirect("/user/login/page");
            return false;
        }

        boolean result = JwtUtils.checkSignature(token);
        if(!result){
            throw new ShipException(ShipExceptionEnum.TOKEN_ERROR);
        }

        PayLoad payload = JwtUtils.getPayLoad(token);
        request.setAttribute("currUser", payload.getUserName());
        return true;
    }

}
