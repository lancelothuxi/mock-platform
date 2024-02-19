package io.github.lancelothuxi.mock.examples.dubbo.apache;

public class Application {

    public static void main(String[] args) throws Exception {

        DubboApacheExampleService dubboApacheExampleService =new DubboApacheExampleService();
        String sayHelloByDubbo = dubboApacheExampleService.sayHelloByDubbo();

        //输出： sayHelloByDubbo =  this is mock data
        System.out.println("sayHelloByDubbo = " + sayHelloByDubbo);
    }
}