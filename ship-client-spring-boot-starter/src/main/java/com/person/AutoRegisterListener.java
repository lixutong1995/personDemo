package com.person;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.person.constants.AdminConstants;
import com.person.constants.NacosConstants;
import com.person.exception.ShipException;
import com.person.pojo.dto.RegisterAppDTO;
import com.person.pojo.dto.UnRegisterAppDTO;
import com.person.utils.IpUtil;
import com.person.utils.OkhttpTool;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 在项目启动时做以下两件事
 * 1、将服务信息注册到Nacos注册中心
 * 2、通知ship-admin服务上线了并注册下线hook
 * @Author Xutong Li
 * @Date 2021/2/8
 */
@Slf4j
public class AutoRegisterListener implements ApplicationListener<ContextRefreshedEvent> {

    private final static Logger logger = LoggerFactory.getLogger(AutoRegisterListener.class);

    /**
     * AtomicBoolean 可用在应用程序中（如以原子方式更新的标志），但不能用于替换 Boolean。
     * Atomic就是原子性的意思，即能够保证在高并发的情况下只有一个线程能够访问这个属性值。
     */
    private volatile AtomicBoolean registered = new AtomicBoolean(false);

    /**
     * 客户端配置
     */
    private final ClientConfigProperties properties;

    /**
     * nacos命名服务类
     */
    @NacosInjected
    private NamingService namingService;


    /**
     * 为在标注@Controller注解的类中的@RequestMapping注解创建RequestMappingInfo实例
     * RequestMappingInfo:保存请求映射信息的类。如：请求方法、请求参数、请求头信息等。
     */
    @Autowired
    private RequestMappingInfoHandlerMapping handlerMapping;

    /**
     * 线程池接口
     */
    private final ExecutorService pool;

    /**
     * 需要忽略的 URL请求地址集合
     */
    private static List<String> ignoreUrlList = Lists.newLinkedList();

    static{
        ignoreUrlList.add("/error");
    }

    public AutoRegisterListener(ClientConfigProperties properties) {
        if(!check(properties)){
            logger.error("客户端配置 port、contextPath、appName、adminUrl、version 不能为空");
            throw new ShipException("客户端配置 port、contextPath、appName、adminUrl、version 不能为空");
        }
        this.properties = properties;
        this.pool = new ThreadPoolExecutor(1, 4, 0, TimeUnit.SECONDS, new LinkedBlockingDeque<>());
    }

    /**
     * @Description 校验配置的资源文件
     * @Author Xutong Li
     * @Date 2021/3/5
     * @param properties
     * @return boolean
     */
    private boolean check(ClientConfigProperties properties) {
        if(properties.getPort() == null || properties.getContextPath() == null
                || properties.getVersion() == null || properties.getAppName() == null
                || properties.getAdminUrl() == null){
            return false;
        }
        return true;
    }

    /**
     * @Description 需要执行的逻辑代码，当spring容器初始化完成后就会执行该方法。
     * @Author Xutong Li
     * @Date 2021/3/5
     * @param event
     * @return
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(!registered.compareAndSet(false,true)){
            return;
        }
        //将所有的接口信息注册到注册中心
        doRegister();
        //当jvm关闭时，向管理员发送注销请求
        registerShutDownHook();
    }

    /**
     * @Description 当jvm关闭时，向管理员发送注销请求
     * @Author Xutong Li
     * @Date 2021/3/5
     * @param        
     * @return void
     */
    private void registerShutDownHook() {
        final String url = "http://" + properties.getAdminUrl() + AdminConstants.UNREGISTER_PATH;
        final UnRegisterAppDTO unRegisterAppDTO = new UnRegisterAppDTO();
        unRegisterAppDTO.setAppName(properties.getAppName());
        unRegisterAppDTO.setVersion(properties.getVersion());
        unRegisterAppDTO.setIp(IpUtil.getLocalIpAddress());
        unRegisterAppDTO.setPort(properties.getPort());
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            OkhttpTool.doPost(url, unRegisterAppDTO);
            logger.info("[{},{}] 从 ship-admin 注销成功", unRegisterAppDTO.getAppName(), unRegisterAppDTO.getVersion());
        }));
    }

    /**
     * @Description 将所有的接口信息注册到注册中心
     * @Author Xutong Li
     * @Date 2021/3/5
     * @param
     * @return void
     */
    private void doRegister() {
        Instance instance = new Instance();
        instance.setIp(IpUtil.getLocalIpAddress());
        instance.setPort(properties.getPort());
        instance.setEphemeral(Boolean.TRUE);
        Map<String, String> metaDataMap = Maps.newHashMap();
        metaDataMap.put("version", properties.getVersion());
        metaDataMap.put("appName", properties.getAppName());
        instance.setMetadata(metaDataMap);
        try {
            namingService.registerInstance(properties.getAppName(), NacosConstants.APP_GROUP_NAME, instance);
        } catch (NacosException e) {
            logger.error(e.getErrMsg(), e);
            throw new ShipException(e.getErrCode(), e.getErrMsg());
        }
        logger.info("接口信息注册到 nacos 成功");
        String url = "http://" + properties.getAdminUrl() + AdminConstants.REGISTER_PATH;
        RegisterAppDTO registerAppDTO = buildRegisterAppDTO(instance);
        OkhttpTool.doPost(url, registerAppDTO);
        logger.info("注册到 ship-admin 成功");

    }

    /**
     * @Description 构建 RegisterAppDTO 实体类
     * @Author Xutong Li
     * @Date 2021/3/5
     * @param instance
     * @return com.person.pojo.dto.RegisterAppDTO
     */
    private RegisterAppDTO buildRegisterAppDTO(Instance instance) {
        RegisterAppDTO registerAppDTO = new RegisterAppDTO();
        registerAppDTO.setAppName(properties.getAppName());
        registerAppDTO.setContextPath(properties.getContextPath());
        registerAppDTO.setIp(instance.getIp());
        registerAppDTO.setPort(instance.getPort());
        registerAppDTO.setVersion(properties.getVersion());
        return registerAppDTO;
    }
}
