package cm.study.java.lang.iface;

public interface Face2 {

    int sayHello(int times);

    default String greeting(String name) {
        return "Hello, " + name;
    }

}
