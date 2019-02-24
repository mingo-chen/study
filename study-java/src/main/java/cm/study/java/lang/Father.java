package cm.study.java.lang;

public class Father {

    static {
        System.out.println("Father static init...");
    }

    public Father() {
        System.out.println("Father init...");
    }

    public void call() {
        System.out.println("Father call some thing!");
    }
}
