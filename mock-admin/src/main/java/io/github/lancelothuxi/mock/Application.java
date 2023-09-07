package io.github.lancelothuxi.mock;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.apache.dubbo.config.spring.context.annotation.EnableDubboConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 *
 * @author lancelot huxisuz@gmail.com
 */
@SpringBootApplication
@EnableDubbo
@EnableDubboConfig
@MapperScan(basePackages = {"io.github.lancelothuxi.mock.mapper","io.github.lancelothuxi.mock.system.mapper"})
public class Application {

  public static void main(String[] args) {

    SpringApplication.run(Application.class, args);
  }
}
