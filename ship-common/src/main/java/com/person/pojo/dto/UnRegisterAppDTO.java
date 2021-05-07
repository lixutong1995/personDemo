package com.person.pojo.dto;

/**
 * @Description UnRegisterApp实体数据传输对象
 * @Author Xutong Li
 * @Date 2021/3/8
 */
public class UnRegisterAppDTO {

    private String appName;

    private String version;

    private String ip;

    private Integer port;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
