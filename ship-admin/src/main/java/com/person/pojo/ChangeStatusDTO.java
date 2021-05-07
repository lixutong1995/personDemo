package com.person.pojo;

/**
 * @Description 应用启用状态DTO
 * @Author Xutong Li
 * @Date 2021/5/1
 */
public class ChangeStatusDTO {

    private Integer id;

    private Byte enabled;

    private String appName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Byte getEnabled() {
        return enabled;
    }

    public void setEnabled(Byte enabled) {
        this.enabled = enabled;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
