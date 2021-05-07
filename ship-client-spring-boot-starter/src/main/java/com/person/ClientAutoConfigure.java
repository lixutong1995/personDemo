package com.person;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @Description 客户端自动配置
 * @Author Xutong Li
 * @Date 2021/3/8
 */
@Configuration
@Import(value = {AutoRegisterListener.class})
@EnableConfigurationProperties(value = {ClientConfigProperties.class})
public class ClientAutoConfigure {
}
