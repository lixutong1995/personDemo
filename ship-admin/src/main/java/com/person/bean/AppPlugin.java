package com.person.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Description 应用插件实体类
 * @Author Xutong Li
 * @Date 2021/3/9
 */
@TableName("t_app_plugin")
@Data
public class AppPlugin {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer appId;

    private Integer pluginId;

    private Byte enabled;
}
