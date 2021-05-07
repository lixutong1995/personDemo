package com.person.transfer;

import com.person.bean.AppInstance;
import com.person.pojo.dto.ServiceInstanceDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @Description 服务实例转换接口
 * @Author Xutong Li
 * @Date 2021/5/1
 */
public interface AppInstanceTransfer {

    AppInstanceTransfer INSTANCE = Mappers.getMapper(AppInstanceTransfer.class);

    ServiceInstanceDTO mapToService(AppInstance appInstance);

    List<ServiceInstanceDTO> mapToServiceList(List<AppInstance> appInstanceList);
}
