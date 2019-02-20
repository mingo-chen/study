package cm.study.dubbo.greet.provider;

import cm.study.dubbo.greet.GreetService;

public class GreetServiceImpl implements GreetService {

    @Override
    public String sayHello(String name) {
        System.out.println("---> provider receive request: " + name);
        return "Hello " + name;
    }
}
