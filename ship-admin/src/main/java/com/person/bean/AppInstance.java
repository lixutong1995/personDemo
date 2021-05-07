package com.person.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Description 应用情况实体类
 * @Author Xutong Li
 * @Date 2021/3/9
 */
@TableName("t_app_instance")
@Data
public class AppInstance {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer appId;

    private String version;

    private String ip;

    private Integer port;

    private Integer weight;

    private LocalDateTime createdTime;
}
