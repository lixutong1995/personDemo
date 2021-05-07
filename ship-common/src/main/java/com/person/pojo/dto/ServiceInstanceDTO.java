package com.person.pojo.dto;

/**
 * @Description 服务实例数据传输实体类
 * @Author Xutong Li
 * @Date 2021/5/1
 */
public class ServiceInstanceDTO {

    private String appName;

    private String ip;

    private Integer port;

    private String version;

    private Integer weight;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }
}
