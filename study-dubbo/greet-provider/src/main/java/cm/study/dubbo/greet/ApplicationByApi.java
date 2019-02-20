package cm.study.dubbo.greet;

import cm.study.dubbo.greet.provider.GreetServiceImpl;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.ServiceConfig;

/**
 * 通过API方式注册服务
 */
public class ApplicationByApi {

    public static void main(String[] args) throws Exception {
        ServiceConfig<GreetService> greetServiceServiceConfig = new ServiceConfig<>();
        greetServiceServiceConfig.setApplication(new ApplicationConfig("app-greeting-provider"));
        greetServiceServiceConfig.setRegistry(new RegistryConfig("zookeeper://127.0.0.1:2181"));
        greetServiceServiceConfig.setInterface(GreetService.class);
        greetServiceServiceConfig.setRef(new GreetServiceImpl());
        greetServiceServiceConfig.export();

        System.in.read(); // block
    }
}
