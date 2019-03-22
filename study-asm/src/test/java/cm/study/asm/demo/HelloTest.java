package cm.study.asm.demo;

import java.lang.reflect.Method;
import java.rmi.MarshalException;

public class HelloTest {

    public void sayHello(String name) {
        System.out.println("Hi, " + name + "!");
    }

    public static void main(String[] args) throws Exception {
        HelloTest test = new HelloTest();
        test.sayHello("cm");

        Method[] methods = test.getClass().getDeclaredMethods();
        for (Method method : methods) {
            System.out.println("--> " + method);
        }

    }
}
