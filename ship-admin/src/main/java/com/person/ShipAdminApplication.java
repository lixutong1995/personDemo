package com.person;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Description ship-admin启动类
 * @Author Xutong Li
 * @Date 2021/3/8
 */
@MapperScan(basePackages = {"com.person.mapper"})
@SpringBootApplication(scanBasePackages = {"com.person"})
@Slf4j
public class ShipAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShipAdminApplication.class, args);
        log.info("======================ship-admin启动成功======================================");
    }
}
