package com.person.pojo.dto;

import java.util.List;

/**
 * @Description 服务信息数据传输实体类
 * @Author Xutong Li
 * @Date 2021/5/1
 */
public class AppInfoDTO {

    private Integer appId;

    private String appName;

    private Byte enabled;

    private List<ServiceInstanceDTO> serviceInstanceDTOList;

    private List<String> enabledPlugins;

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Byte getEnabled() {
        return enabled;
    }

    public void setEnabled(Byte enabled) {
        this.enabled = enabled;
    }

    public List<ServiceInstanceDTO> getServiceInstanceDTOList() {
        return serviceInstanceDTOList;
    }

    public void setServiceInstanceDTOList(List<ServiceInstanceDTO> serviceInstanceDTOList) {
        this.serviceInstanceDTOList = serviceInstanceDTOList;
    }

    public List<String> getEnabledPlugins() {
        return enabledPlugins;
    }

    public void setEnabledPlugins(List<String> enabledPlugins) {
        this.enabledPlugins = enabledPlugins;
    }
}
