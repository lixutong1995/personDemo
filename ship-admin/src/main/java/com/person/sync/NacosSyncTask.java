package com.person.sync;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.ListView;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.person.constants.EnabledEnum;
import com.person.constants.NacosConstants;
import com.person.pojo.NacosMetaData;
import com.person.pojo.dto.AppInfoDTO;
import com.person.pojo.dto.ServiceInstanceDTO;
import com.person.service.IAppService;
import com.person.utils.OkhttpTool;
import com.person.utils.StringTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;


/**
 * @Description nacos同步任务
 * @Author Xutong Li
 * @Date 2021/5/1
 */
public class NacosSyncTask implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(NacosSyncTask.class);

    private NamingService namingService;

    private String url;

    private IAppService appService;

    private Gson gson = new GsonBuilder().create();

    public NacosSyncTask(NamingService namingService, String url, IAppService appService) {
        this.namingService = namingService;
        this.url = url;
        this.appService = appService;
    }

    /**
     * @Description 定期更新nacos实例的权重，启用的插件，启用的状态
     * @Author Xutong Li
     * @Date 2021/5/1
     * @return void
     */
    @Override
    public void run() {
        try {
            //获取所有服务名称
            //NacosNamingService的getServicesOfServer方法请求的是serverProxy.getServiceList，
            // 其pageNo从1开始；NamingProxy的getServiceList方法请求的是/service/list接口，
            // 它将返还的数据组装为ListView结构；ListView包含了count属性及List类型的data属性
            ListView<String> services = namingService.getServicesOfServer(1, Integer.MAX_VALUE, NacosConstants.APP_GROUP_NAME);
            if(CollectionUtils.isEmpty(services.getData())){
                return;
            }
            List<String> appNameList = services.getData();
            List<AppInfoDTO> appInfoDTOList = appService.getAppInfos(appNameList);
            for(AppInfoDTO appInfoDTO : appInfoDTOList){
                if(CollectionUtils.isEmpty(appInfoDTO.getServiceInstanceDTOList())){
                    continue;
                }
                for(ServiceInstanceDTO instanceDTO : appInfoDTO.getServiceInstanceDTOList()){
                    Map<String, Object> queryMap = buildQueryMap(appInfoDTO, instanceDTO);
                    String responseStr = OkhttpTool.doPut(url, queryMap, "");
                    LOGGER.debug("response {}", responseStr);
                }
            }
        } catch (NacosException e) {
            LOGGER.error("nacos 同步任务失败", e);
        }
    }

    /**
     * @Description 构建查询条件Map
     * @Author Xutong Li
     * @Date 2021/5/7
     * @param appInfoDTO
     * @param instanceDTO
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    private Map<String, Object> buildQueryMap(AppInfoDTO appInfoDTO, ServiceInstanceDTO instanceDTO) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("serviceName", appInfoDTO.getAppName());
        map.put("groupName", NacosConstants.APP_GROUP_NAME);
        map.put("ip", instanceDTO.getIp());
        map.put("port", instanceDTO.getPort());
        map.put("weight", instanceDTO.getWeight().doubleValue());
        NacosMetaData nacosMetaData = new NacosMetaData();
        nacosMetaData.setAppName(appInfoDTO.getAppName());
        nacosMetaData.setVersion(instanceDTO.getVersion());
        nacosMetaData.setPlugins(String.join(",", appInfoDTO.getEnabledPlugins()));
        map.put("metadata", StringTools.urlEncode(gson.toJson(nacosMetaData)));
        map.put("ephemeral", true);
        map.put("enabled", EnabledEnum.ENABLE.getCode().equals(appInfoDTO.getEnabled()));
        return map;
    }
}
