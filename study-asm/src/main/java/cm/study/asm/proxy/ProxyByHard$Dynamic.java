package cm.study.asm.proxy;

public class ProxyByHard$Dynamic extends Target {

    private Target target;

    public ProxyByHard$Dynamic(Target init) {
        target = init;
    }

    public void doSomething(String id) {
        System.out.println("start init work...");

        target.doSomething(id);

        System.out.println("start clean work...");
    }
}
