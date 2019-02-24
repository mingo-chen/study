package cm.study.java.lang;

public class Son extends Father {
    static {
        System.out.println("Son static init...");
    }

    public Son() {
        System.out.println("Son init...");
    }

    @Override
    public void call() {
        System.out.println("Son call some thing!");
    }

    public static void main(String[] args) {
        Father son = new Father();

        son.call();
    }
}
