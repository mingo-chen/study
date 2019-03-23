package cm.study.asm.proxy;

import cm.study.asm.common.Target;

public class ProxyByHard$Dynamic extends Target {

    private Target target;

    public ProxyByHard$Dynamic(Target init) {
        target = init;
    }

    public boolean doSomething(String id) {
        System.out.println("start init work...");

        boolean ret = target.doSomething(id);

        System.out.println("start clean work...");
        return ret;
    }

//    @Override
//    public byte doSomething(String id, String name, int age, int size) {
//        System.out.println("start init work...");
//
//        byte ret = target.doSomething(id, name, age, size);
//
//        System.out.println("start clean work...");
//        return ret;
//    }

    @Override
    public void otherThing(String id, String name) {
        System.out.println("start init work...");

        target.otherThing(id, name);

        System.out.println("start clean work...");
    }
}
