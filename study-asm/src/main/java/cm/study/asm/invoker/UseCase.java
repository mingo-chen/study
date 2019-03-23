package cm.study.asm.invoker;

import cm.study.asm.common.Target;

public class UseCase {

    public static void main(String[] args) {
        InvokerFactory.scan(Target.class, new Target());

        testDoSomething1();
        testDoSomething3();
        testOtherThing();
        testNotExist();
    }

    public static void testDoSomething1() {
        Invoker invoker = InvokerFactory.get(Target.class);
        FormInvocation invocation = new FormInvocation();
        invocation.setMethodName("doSomething");
        invocation.with("1234");

        Object result = invoker.call(invocation);
        System.out.println("result " + result);
    }

    public static void testDoSomething3() {
        Invoker invoker = InvokerFactory.get(Target.class);
        FormInvocation invocation = new FormInvocation();
        invocation.setMethodName("doSomething");
        invocation.with("1234", "xxxx", 30, 128);

        Object result = invoker.call(invocation);
        System.out.println("result " + result);
    }

    public static void testOtherThing() {
        Invoker invoker = InvokerFactory.get(Target.class);
        FormInvocation invocation = new FormInvocation();
        invocation.setMethodName("otherThing");
        invocation.with("1234", "xxxx");

        Object result = invoker.call(invocation);
        System.out.println("result " + result);
    }

    public static void testNotExist() {
        Invoker invoker = InvokerFactory.get(Target.class);
        FormInvocation invocation = new FormInvocation();
        invocation.setMethodName("notExistMethod");
        invocation.with("1234", "xxxx");

        Object result = invoker.call(invocation);
        System.out.println("result " + result);
    }
}
