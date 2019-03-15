package cm.study.dubbo.greet;

import cm.study.dubbo.greet.provider.GreetServiceImpl;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.ServiceConfig;

public class InjvmByApi {

    public void startProvider(ApplicationConfig app) {
        ServiceConfig<GreetService> greetServiceServiceConfig = new ServiceConfig<>();
        greetServiceServiceConfig.setApplication(app);
//        greetServiceServiceConfig.setRegistry(new RegistryConfig("local-injvm"));   // 随便写
        greetServiceServiceConfig.setRegistry(new RegistryConfig("zookeeper://127.0.0.1:2181"));
//        greetServiceServiceConfig.setScope("local");    // Provider默认就导出到本地和注册中心
        greetServiceServiceConfig.setInterface(GreetService.class);
        greetServiceServiceConfig.setRef(new GreetServiceImpl());
        greetServiceServiceConfig.export();
    }

    public void startConsumer(ApplicationConfig app) {
        ReferenceConfig<GreetService> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setApplication(app);
        referenceConfig.setRegistry(new RegistryConfig("local-injvm")); // 随便写
        referenceConfig.setInterface(GreetService.class);
        referenceConfig.setScope("local");  // scope必须是local
        GreetService greetService = referenceConfig.get();
        String result = greetService.sayHello("dubbo");
        System.out.println("--> " + result);
    }

    public static void main(String[] args) {
        ApplicationConfig app = new ApplicationConfig("dubbo-injvm");
        InjvmByApi injvm = new InjvmByApi();
        injvm.startProvider(app);
        injvm.startConsumer(app);

    }
}
