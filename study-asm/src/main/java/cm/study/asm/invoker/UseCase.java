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
        invocation.setName("doSomething");
        invocation.with("id", "1234");

        Object result = invoker.call(invocation);
        System.out.println("result " + result);
    }

    public static void testDoSomething3() {
        Invoker invoker = InvokerFactory.get(Target.class);
        FormInvocation invocation = new FormInvocation();
        invocation.setName("doSomething");
        invocation.with("id", "1234");
        invocation.with("name", "xxxx");
        invocation.with("age", "30");

        Object result = invoker.call(invocation);
        System.out.println("result " + result);
    }

    public static void testOtherThing() {
        Invoker invoker = InvokerFactory.get(Target.class);
        FormInvocation invocation = new FormInvocation();
        invocation.setName("otherThing");
        invocation.with("id", "1234");
        invocation.with("name", "xxxx");

        Object result = invoker.call(invocation);
        System.out.println("result " + result);
    }

    public static void testNotExist() {
        Invoker invoker = InvokerFactory.get(Target.class);
        FormInvocation invocation = new FormInvocation();
        invocation.setName("notExistMethod");
        invocation.with("id", "1234");
        invocation.with("name", "xxxx");

        Object result = invoker.call(invocation);
        System.out.println("result " + result);
    }
}
