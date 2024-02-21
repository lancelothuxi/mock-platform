package io.github.lancelothuxi.mock.examples;

import feign.Contract;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import io.github.lancelothuxi.mock.examples.dubbo.apache.DubboApacheExampleService;
import io.github.lancelothuxi.mock.examples.feign.HelloFeignService;

public class Main {
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
        System.out.println("cal result = " + result);

        DubboApacheExampleService dubboApacheExampleService =new DubboApacheExampleService();
        String sayHelloByDubbo = dubboApacheExampleService.sayHelloByDubbo();

        //输出： sayHelloByDubbo =  this is mock data
        System.out.println("sayHelloByDubbo = " + sayHelloByDubbo);

        HelloFeignService feignService = Feign.builder().encoder(new JacksonEncoder()).decoder(new JacksonDecoder())
                .contract(new Contract.Default())
                .target(HelloFeignService.class, "http://localhost");

        System.out.println("feignService.hello() = " + feignService.hello());
    }
}
