package com.person.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.person.bean.App;
import com.person.bean.AppInstance;
import com.person.bean.AppPlugin;
import com.person.bean.Plugin;
import com.person.constants.EnabledEnum;
import com.person.exception.ShipException;
import com.person.mapper.AppInstanceMapper;
import com.person.mapper.AppMapper;
import com.person.mapper.AppPluginMapper;
import com.person.mapper.PluginMapper;
import com.person.pojo.AppPluginDTO;
import com.person.pojo.ChangeStatusDTO;
import com.person.pojo.dto.AppInfoDTO;
import com.person.pojo.dto.RegisterAppDTO;
import com.person.pojo.dto.ServiceInstanceDTO;
import com.person.pojo.dto.UnRegisterAppDTO;
import com.person.pojo.vo.AppVO;
import com.person.service.IAppService;
import com.person.transfer.AppInstanceTransfer;
import com.person.transfer.AppVoTransfer;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description 应用实现层
 * @Author Xutong Li
 * @Date 2021/3/8
 */
@Service
@Slf4j
public class AppServiceImpl implements IAppService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppServiceImpl.class);

    @Resource
    private AppMapper appMapper;

    @Resource
    private PluginMapper pluginMapper;

    @Resource
    private AppPluginMapper appPluginMapper;

    @Resource
    private AppInstanceMapper appInstanceMapper;

    private Gson gson = new GsonBuilder().create();


    /**
     * @Description 应用注册
     * @Author Xutong Li
     * @Date 2021/3/8
     * @param registerAppDTO
     * @return void
     */
    @Override
    public void register(RegisterAppDTO registerAppDTO) {
        App app = queryByAppName(registerAppDTO.getAppName());
        Integer appId;
        if(app == null){
            //第一次注册
            appId = addApp(registerAppDTO);
        }else{
            appId = app.getId();
        }

        AppInstance instance = query(appId, registerAppDTO.getIp(), registerAppDTO.getPort(), registerAppDTO.getVersion());
        if(instance == null){
            AppInstance appInstance = new AppInstance();
            appInstance.setAppId(appId);
            appInstance.setIp(registerAppDTO.getIp());
            appInstance.setPort(registerAppDTO.getPort());
            appInstance.setVersion(registerAppDTO.getVersion());
            appInstance.setCreatedTime(LocalDateTime.now());
            appInstanceMapper.insert(appInstance);
        }
        log.info("应用注册成功，dto:[{}]", gson.toJson(registerAppDTO));
    }

    /**
     * @Description 应用注销
     * @Author Xutong Li
     * @Date 2021/3/9
     * @param unRegisterAppDTO
     * @return void
     */
    @Override
    public void unRegister(UnRegisterAppDTO unRegisterAppDTO) {
        App app = queryByAppName(unRegisterAppDTO.getAppName());
        if(app == null){
            return;
        }
        QueryWrapper<AppInstance> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(AppInstance::getAppId, app.getId())
                .eq(AppInstance::getVersion, unRegisterAppDTO.getVersion())
                .eq(AppInstance::getIp, unRegisterAppDTO.getIp())
                .eq(AppInstance::getPort, unRegisterAppDTO.getPort());
        appInstanceMapper.delete(wrapper);
        log.info("应用注销成功，dto:[{}]", gson.toJson(unRegisterAppDTO));
    }

    /**
     * @Description 查询应用情况
     * @Author Xutong Li
     * @Date 2021/3/9
     * @param appId
     * @param ip
     * @param port
     * @param version
     * @return com.person.bean.AppInstance
     */
    private AppInstance query(Integer appId, String ip, Integer port, String version) {
        QueryWrapper<AppInstance> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(AppInstance::getId, appId)
                .eq(AppInstance::getPort, port)
                .eq(AppInstance::getId, ip)
                .eq(AppInstance::getVersion, version);
        return appInstanceMapper.selectOne(wrapper);
    }

    /**
     * @Description 新增应用
     * @Author Xutong Li
     * @Date 2021/3/9
     * @param registerAppDTO
     * @return java.lang.Integer
     */
    private Integer addApp(RegisterAppDTO registerAppDTO) {
        App app = new App();
        BeanUtils.copyProperties(registerAppDTO, app);
        app.setEnabled(EnabledEnum.ENABLE.getCode());
        app.setCreatedTime(LocalDateTime.now());
        appMapper.insert(app);
        bindPlugins(app);
        return app.getId();
    }

    /**
     * @Description 绑定应用程序与所有插件
     * @Author Xutong Li
     * @Date 2021/3/9
     * @param app       
     * @return void
     */
    private void bindPlugins(App app) {
        List<Plugin> plugins = pluginMapper.selectList(new QueryWrapper<>());
        if(CollectionUtils.isEmpty(plugins)){
            throw new ShipException("必须初始化一个插件");
        }

        plugins.forEach(plugin -> {
            AppPlugin appPlugin = new AppPlugin();
            appPlugin.setAppId(app.getId());
            appPlugin.setPluginId(plugin.getId());
            appPlugin.setEnabled(EnabledEnum.ENABLE.getCode());
            appPluginMapper.insert(appPlugin);
        });
    }

    /**
     * @Description 通过应用名称查询应用信息
     * @Author Xutong Li
     * @Date 2021/3/8
     * @param appName
     * @return com.person.bean.App
     */
    private App queryByAppName(String appName) {
        QueryWrapper<App> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(App::getAppName, appName);
        App app = appMapper.selectOne(wrapper);
        return app;
    }

    /**
     * @Description 查询应用集合
     * @Author Xutong Li
     * @Date 2021/5/1
     * @param        
     * @return java.util.List<com.person.pojo.vo.AppVO>
     */
    @Override
    public List<AppVO> getList(){
        QueryWrapper<App> wrapper = new QueryWrapper<>();
        List<App> appList = appMapper.selectList(wrapper);
        if(CollectionUtils.isEmpty(appList)){
            return Lists.newArrayList();
        }
        List<AppVO> appVOList = AppVoTransfer.INSTANCE.mapToVOList(appList);
        List<Integer> appIds = appVOList.stream().map(AppVO::getId).collect(Collectors.toList());
        QueryWrapper<AppInstance> query = Wrappers.query();
        query.lambda().in(AppInstance::getAppId, appIds);
        List<AppInstance> appInstanceList = appInstanceMapper.selectList(query);
        if(CollectionUtils.isEmpty(appInstanceList)){
            appVOList.forEach(appVO -> {
                appVO.setInstanceNum(0);
            });
        }else {
            Map<Integer, List<AppInstance>> map = appInstanceList.stream().collect(Collectors.groupingBy(AppInstance::getId));
            appVOList.forEach(appVO -> {
                appVO.setInstanceNum(map.getOrDefault(appVO.getId(), Lists.newArrayList()).size());
            });
        }
        return  appVOList;
    }

    /**
     * @Description 如果应用禁用，则所以应用实例全部下线
     * @Author Xutong Li
     * @Date 2021/5/1
     * @param changeStatusDTO       
     * @return void
     */
    @Override
    public void updateEnabled(ChangeStatusDTO changeStatusDTO) {
        App app = new App();
        app.setId(changeStatusDTO.getId());
        app.setEnabled(changeStatusDTO.getEnabled());
        appMapper.updateById(app);
    }

    /**
     * @Description 删除服务
     * @Author Xutong Li
     * @Date 2021/5/1
     * @param id
     * @return void
     */
    @Override
    public void delete(Integer id) {
        QueryWrapper<AppInstance> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(AppInstance::getAppId, id);
        List<AppInstance> appInstanceList = appInstanceMapper.selectList(wrapper);
        if(!CollectionUtils.isEmpty(appInstanceList)){
            throw new ShipException("该服务存在实例不可删除！");
        }
        appMapper.deleteById(id);
    }

    /**
     * @Description 获取服务信息集合（通过服务名集合）
     * @Author Xutong Li
     * @Date 2021/5/1
     * @param appNameList
     * @return java.util.List<com.person.pojo.dto.AppInfoDTO>
     */
    @Override
    public List<AppInfoDTO> getAppInfos(List<String> appNameList) {
        List<App> appList = getAppList(appNameList);
        List<Integer> appIdList = appList.stream().map(App::getId).collect(Collectors.toList());
        QueryWrapper<AppInstance> wrapper = Wrappers.query();
        wrapper.lambda().in(AppInstance::getAppId, appIdList);
        List<AppInstance> appInstanceList = appInstanceMapper.selectList(wrapper);
        List<AppPluginDTO> appPluginDTOList = appPluginMapper.queryEnabledPlugins(appIdList);
        if(CollectionUtils.isEmpty(appInstanceList) || CollectionUtils.isEmpty(appPluginDTOList)){
            LOGGER.info("没有服务信息！！！");
            return Lists.newArrayList();
        }
        return buildAppInfos(appList, appInstanceList, appPluginDTOList);
    }

    /**
     * @Description 组装服务信息集合
     * @Author Xutong Li
     * @Date 2021/5/1
     * @param appList
     * @param appInstanceList
     * @param appPluginDTOList
     * @return java.util.List<com.person.pojo.dto.AppInfoDTO>
     */
    private List<AppInfoDTO> buildAppInfos(List<App> appList, List<AppInstance> appInstanceList, List<AppPluginDTO> appPluginDTOList) {
        List<AppInfoDTO> list = Lists.newArrayList();
        appList.forEach(app -> {
            AppInfoDTO appInfoDTO = new AppInfoDTO();
            appInfoDTO.setAppId(app.getId());
            appInfoDTO.setAppName(app.getAppName());
            appInfoDTO.setEnabled(app.getEnabled());
            List<String> enabledPlugins = appPluginDTOList.stream().filter(r -> r.getAppId().equals(app.getId()))
                    .map(AppPluginDTO::getCode).collect(Collectors.toList());
            appInfoDTO.setEnabledPlugins(enabledPlugins);
            List<AppInstance> instanceList = appInstanceList.stream().filter(r -> r.getAppId().equals(app.getId())).collect(Collectors.toList());
            List<ServiceInstanceDTO> serviceInstanceDTOList = AppInstanceTransfer.INSTANCE.mapToServiceList(instanceList);
            appInfoDTO.setServiceInstanceDTOList(serviceInstanceDTOList);
            list.add(appInfoDTO);
        });
        return list;
    }

    /**
     * @Description 获取服务集合（通过服务名）
     * @Author Xutong Li
     * @Date 2021/5/1
     * @param appNameList
     * @return java.util.List<com.person.bean.App>
     */
    private List<App> getAppList(List<String> appNameList) {
        QueryWrapper<App> wrapper = Wrappers.query();
        wrapper.lambda().in(App::getAppName, appNameList);
        return appMapper.selectList(wrapper);
    }


}
