package com.person;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Description 客户端配置实体类
 * @Author Xutong Li
 * @Date 2021/2/8
 */
@ConfigurationProperties(prefix = "ship.http")
public class ClientConfigProperties {

    /**
     * 启动端口号
     */
    private Integer port;

    /**
     * 请求根路径
     */
    private String contextPath;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 接口版本号
     */
    private String version;

    /**
     * admin的 URL 地址
     */
    private String adminUrl;

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

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

    public String getAdminUrl() {
        return adminUrl;
    }

    public void setAdminUrl(String adminUrl) {
        this.adminUrl = adminUrl;
    }
}
