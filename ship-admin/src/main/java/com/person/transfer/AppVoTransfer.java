package com.person.transfer;

import com.person.bean.App;
import com.person.pojo.vo.AppVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @Description 应用实体类vo转换接口
 * @Author Xutong Li
 * @Date 2021/5/1
 */
@Mapper
public interface AppVoTransfer {

    AppVoTransfer INSTANCE = Mappers.getMapper(AppVoTransfer.class);

    @Mappings({
            @Mapping(target = "createdTime", expression = "java(com.person.utils.DateUtils.formatterToYYYYMMDDHHmmss(app.getCreatedTime()))")
    })
    AppVO mapToVO(App app);

    List<AppVO> mapToVOList(List<App> appList);
}
