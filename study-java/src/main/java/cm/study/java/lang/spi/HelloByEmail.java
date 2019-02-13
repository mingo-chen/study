package cm.study.java.lang.spi;

public class HelloByEmail implements HelloService {

    @Override
    public void hi(String target) {
        System.out.println("---> 使用邮件打来招呼");
    }
}
