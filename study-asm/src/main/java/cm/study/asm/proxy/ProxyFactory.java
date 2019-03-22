package cm.study.asm.proxy;

public class ProxyFactory {

    public static <T> T wrapper(Target target) {
        // 对target进行包装, 增强功能
        return (T) new ProxyByAsm(target).wrapper();
    }

    public static void main(String[] args) {
        Target target = new Target();
        Target proxy = ProxyFactory.wrapper(target);
        proxy.doSomething("001");
    }
}
