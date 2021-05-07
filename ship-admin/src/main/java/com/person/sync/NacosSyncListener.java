package com.person.sync;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.naming.NamingService;
import com.person.constants.NacosConstants;
import com.person.service.IAppService;
import com.person.utils.ShipThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

import javax.annotation.Resource;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description Nacos同步监听
 * @Author Xutong Li
 * @Date 2021/5/1
 */
@Configuration
public class NacosSyncListener implements ApplicationListener<ContextRefreshedEvent> {

    private static ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1,
            new ShipThreadFactory("nacos-sync", true).create());

    @NacosInjected
    private NamingService namingService;

    @Value("${nacos.discovery.server-addr}")
    private String baseUrl;

    @Resource
    private IAppService appService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(event.getApplicationContext().getParent() != null){
            return;
        }

        String url = "http://" + baseUrl + NacosConstants.INSTANCE_UPDATE_PATH;
        scheduledThreadPoolExecutor.scheduleWithFixedDelay(new NacosSyncTask(namingService, url, appService), 0, 30L, TimeUnit.SECONDS);
    }
}
