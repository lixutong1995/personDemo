package com.person.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.person.Utils.JwtUtils;
import com.person.bean.User;
import com.person.constants.AdminConstants;
import com.person.constants.ShipExceptionEnum;
import com.person.exception.ShipException;
import com.person.mapper.UserMapper;
import com.person.pojo.PayLoad;
import com.person.pojo.UserDTO;
import com.person.service.IUserService;
import com.person.utils.StringTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @Description 用户管理实现类
 * @Author Xutong Li
 * @Date 2021/5/7
 */
@Service
public class UserServiceImpl implements IUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Resource
    private UserMapper userMapper;

    @Value("${ship.user-password-salt}")
    private String salt;


    /**
     * 用户注册
     * @Author Xutong Li
     * @Date 2021/5/7
     * @param userDTO
     * @return void
     */
    @Override
    public void addUser(UserDTO userDTO) {
        User oldOne = queryByName(userDTO.getUserName());
        if(!Objects.isNull(oldOne)){
            LOGGER.info("{},这个用户已经存在！",userDTO.getUserName());
            throw new ShipException("the userName already exist");
        }
        User user = new User();
        user.setUserName(userDTO.getUserName());
        user.setPassword(StringTools.Md5Digest(userDTO.getPassword(), salt));
        user.setCreatedTime(LocalDateTime.now());
        userMapper.insert(user);
    }

    /**
     * 用户登录
     * @Author Xutong Li
     * @Date 2021/5/7
     * @param userDTO
     * @param response
     * @return void
     */
    @Override
    public void login(UserDTO userDTO, HttpServletResponse response) {
        User user = queryByName(userDTO.getUserName());
        if(Objects.isNull(user)){
            throw new ShipException(ShipExceptionEnum.LOGIN_ERROR);
        }
        String password = StringTools.Md5Digest(userDTO.getPassword(), salt);
        if(!user.getPassword().equals(password)){
            throw new ShipException(ShipExceptionEnum.LOGIN_ERROR);
        }
        PayLoad payLoad = new PayLoad(user.getId(), user.getUserName());
        try {
            String token = JwtUtils.generateToken(payLoad);
            Cookie cookie = new Cookie(AdminConstants.TOKEN_NAME, token);
            cookie.setHttpOnly(true);
            //cookie有效时间30mins
            cookie.setMaxAge(30*60);
            cookie.setPath("/");
            response.addCookie(cookie);
        } catch (Exception e) {
            LOGGER.error("登录失败", e);
            e.printStackTrace();
        }
    }

    /**
     * 通过用户名称查询用户信息
     * @Author Xutong Li
     * @Date 2021/5/7
     * @param userName
     * @return com.person.bean.User
     */
    private User queryByName(String userName) {
        QueryWrapper<User> wrapper = Wrappers.query();
        wrapper.lambda().eq(User::getUserName,userName);
        return userMapper.selectOne(wrapper);
    }


}
