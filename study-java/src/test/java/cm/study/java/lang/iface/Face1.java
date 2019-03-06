package cm.study.java.lang.iface;

public interface Face1 {

    String sayHello();

    default String greeting(String name) {
        return "Hi, " + name;
    }
}
