package com.person.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ThreadFactory;

/**
 * @Description ship线程工厂
 * @Author Xutong Li
 * @Date 2021/5/1
 */
public class ShipThreadFactory {

    private String name;

    /**
     * true 守护线程 主线程结束后守护线程不会继续运行
     * false 用户线程 主线程结束后用户线程还会继续运行
     * @Author Xutong Li
     * @Date 2021/5/1
     * @param null
     * @return
     */
    private boolean daemon;

    public ShipThreadFactory(String name, boolean daemon) {
        this.name = name;
        this.daemon = daemon;
    }

    public ThreadFactory create(){
        return new ThreadFactoryBuilder().setNameFormat(this.name + "-%d").setDaemon(this.daemon).build();
    }
}
