package com.lancelot.mock;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.apache.dubbo.config.spring.context.annotation.EnableDubboConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 * 
 * @author ruoyi
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@EnableDubbo
@EnableDubboConfig
public class Application {


    public static void main(String[] args)
    {

        System.setProperty("zookeeper.sasl.client","false");
        SpringApplication.run(Application.class, args);
    }
}