package com.person.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.person.bean.AppPlugin;
import com.person.pojo.AppPluginDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description 应用插件mapper层
 * @Author Xutong Li
 * @Date 2021/3/9
 */
public interface AppPluginMapper extends BaseMapper<AppPlugin> {

    List<AppPluginDTO> queryEnabledPlugins(@Param("appIds") List<Integer> appIds);
}
