package com.person.pojo;

/**
 * @Description 用户有效信息实体类
 * @Author Xutong Li
 * @Date 2021/5/7
 */
public class PayLoad {

    private Integer userId;

    private String userName;

    public PayLoad(Integer userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
