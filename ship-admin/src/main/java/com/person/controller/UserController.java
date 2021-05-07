package com.person.controller;

import com.person.annotation.NoLog;
import com.person.constants.AdminConstants;
import com.person.pojo.UserDTO;
import com.person.pojo.vo.Result;
import com.person.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description 用户管理controller层
 * @Author Xutong Li
 * @Date 2021/5/7
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    /**
     * 用户注册
     * @Author Xutong Li
     * @Date 2021/5/7
     * @param userDTO
     * @return com.person.pojo.vo.Result
     */
    @PostMapping("/add")
    public Result addUser(@RequestBody @Validated UserDTO userDTO){
        userService.addUser(userDTO);
        return Result.success();
    }

    /**
     * 用户登录
     * @Author Xutong Li
     * @Date 2021/5/7
     * @param userDTO
     * @param response
     * @return void
     */
    @PostMapping("/login")
    public void login(@Validated UserDTO userDTO, HttpServletResponse response) throws IOException {
        userService.login(userDTO, response);
        response.sendRedirect("/app/list");
    }

    /**
     * 访问首页
     * @Author Xutong Li
     * @Date 2021/5/7
     * @param
     * @return java.lang.String
     */
    @NoLog
    @GetMapping("/login/page")
    public String loginPage(){
        return "login";
    }

    /**
     * 退出登录
     * @Author Xutong Li
     * @Date 2021/5/7
     * @param request
     * @param response
     * @return java.lang.String
     */
    @GetMapping("logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        Cookie cookie = new Cookie(AdminConstants.TOKEN_NAME, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "login";
    }
}
