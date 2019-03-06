package cm.study.java.lang.iface;

public class Something implements Face1, Face2 {

    @Override
    public String sayHello() {
        return "";
    }

    @Override
    public int sayHello(int times) {
        return 0;
    }

    @Override
    public String greeting(String name) {
        return Face2.super.greeting(name);
    }
}
