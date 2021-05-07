package com.person.service;

import com.person.pojo.UserDTO;

import javax.servlet.http.HttpServletResponse;

/**
 * @Description 用户管理接口层
 * @Author Xutong Li
 * @Date 2021/5/7
 */
public interface IUserService {

    /**
     * 用户注册
     * @Author Xutong Li
     * @Date 2021/5/7
     * @param userDTO
     * @return void
     */
    void addUser(UserDTO userDTO);

    /**
     * 用户登录
     * @Author Xutong Li
     * @Date 2021/5/7
     * @param userDTO
     * @param response
     * @return void
     */
    void login(UserDTO userDTO, HttpServletResponse response);
}
