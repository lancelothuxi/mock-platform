package io.github.lancelothuxi.mock.examples.feign;

import feign.Contract;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

public class FeignExample {

    public static void main(String[] args) throws Exception{

        HelloFeignService feignService = Feign.builder().encoder(new JacksonEncoder()).decoder(new JacksonDecoder())
                .contract(new Contract.Default())
                .target(HelloFeignService.class, "http://localhost");
        String result = feignService.hello();

        //输出 result =  this is mock data for feign
        System.out.println("result = " + result);
    }
}
