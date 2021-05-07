package com.person.pojo;

/**
 * @Description Nacos元数据实体类
 * @Author Xutong Li
 * @Date 2021/5/7
 */
public class NacosMetaData {

    private String appName;

    private String version;

    private String plugins;

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

    public String getPlugins() {
        return plugins;
    }

    public void setPlugins(String plugins) {
        this.plugins = plugins;
    }
}
