package io.github.lancelothuxi.mock.examples.dubbo.apache;

import io.github.lancelothuxi.mock.examples.Main;
import io.github.lancelothuxi.mock.examples.dubbo.GreetingsService;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DubboApacheExampleService {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public String sayHelloByDubbo(){
        try {
            ReferenceConfig<GreetingsService> reference = new ReferenceConfig<>();
            reference.setApplication(new ApplicationConfig("first-dubbo-consumer"));
            reference.setCheck(false);
            reference.setRegistry(new RegistryConfig("224.5.6.7:1234","multicast"));
            reference.setInterface(GreetingsService.class);
            GreetingsService service = reference.get();
            String message = service.sayHi("dubbo");
            return message;
        }catch (Exception ex){
            logger.error("no provider or internal error...");
            return "no provider or internal error...";
        }
    }
}
