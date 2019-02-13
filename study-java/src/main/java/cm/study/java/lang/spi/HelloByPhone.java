package cm.study.java.lang.spi;

public class HelloByPhone implements HelloService {

    public HelloByPhone() {
        System.out.println("--> init hello by phone....");
    }

    @Override
    public void hi(String target) {
        System.out.println("---> 使用手机打来招呼");
    }
}
