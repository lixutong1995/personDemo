package com.person.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Description 插件实体类
 * @Author Xutong Li
 * @Date 2021/3/9
 */
@TableName("t_plugin")
@Data
public class Plugin {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    private String code;

    private String description;

    private LocalDateTime createdTime;
}
