package io.github.lancelothuxi.mock.examples;

import feign.Contract;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import io.github.lancelothuxi.mock.examples.dubbo.apache.DubboApacheExampleService;
import io.github.lancelothuxi.mock.examples.feign.HelloFeignService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {
        while (true){
           try {
               demoCall();
           }catch (Exception ex){}
           finally {
               try {
                   Thread.sleep(1000);
               } catch (InterruptedException e) {
                   throw new RuntimeException(e);
               }
           }
        }
    }

    private static void demoCall() {
        // 添加 Mock Agent 参数
        // 创建被 mock 的类的实例
        ExampleService service = new ExampleService();
        // 调用被 mock 的方法
        int result = service.add(1, 2);
        logger.info("cal result = " + result);

        DubboApacheExampleService dubboApacheExampleService =new DubboApacheExampleService();
        String sayHelloByDubbo = dubboApacheExampleService.sayHelloByDubbo();

        //输出： sayHelloByDubbo =  this is mock data
        logger.info("sayHelloByDubbo = " + sayHelloByDubbo);
    }
}
