package cm.study.java.lang.spi;

import java.util.Iterator;
import java.util.ServiceLoader;

public class ServiceInvoke {

    public static void main(String[] args) {
        ServiceLoader<HelloService> helloServices = ServiceLoader.load(HelloService.class);
        for(Iterator<HelloService> it = helloServices.iterator(); it.hasNext(); ) {
            HelloService helloService = it.next();
            helloService.hi("cm");
        }
    }
}
