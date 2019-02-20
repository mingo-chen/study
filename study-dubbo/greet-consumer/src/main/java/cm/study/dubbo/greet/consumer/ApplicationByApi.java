package cm.study.dubbo.greet.consumer;

import cm.study.dubbo.greet.GreetService;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;

public class ApplicationByApi {

    public static void main(String[] args) {
        ReferenceConfig<GreetService> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setApplication(new ApplicationConfig("app-greet-consumer"));
        referenceConfig.setRegistry(new RegistryConfig("zookeeper://127.0.0.1:2181"));
        referenceConfig.setInterface(GreetService.class);
        GreetService greetService = referenceConfig.get();
        String result = greetService.sayHello("dubbo");
        System.out.println("--> " + result);
    }
}
