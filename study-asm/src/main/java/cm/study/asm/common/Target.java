package cm.study.asm.common;

public class Target {

    public boolean doSomething(String id) {
        System.out.printf("--> process proposal: id=%s \n", id);
        return true;
    }

    public byte doSomething(String id, String name, int age, int size) {
        System.out.printf("--> process proposal: id=%s, name=%s, age=%d, size=%d \n", id, name, age, size);
        return 1;
    }

    public void otherThing(String id, String name) {
        System.out.printf("--> process other proposal: id=%s, name=%s \n", id, name);
    }
}
