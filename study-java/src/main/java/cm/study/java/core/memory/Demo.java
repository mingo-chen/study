package cm.study.java.core.memory;

import cm.study.java.core.utils.UnsafeKit;
import sun.misc.Unsafe;
import sun.misc.VM;

public class Demo {

    private boolean bool1;
    private boolean bool2;
    private byte byte1;
    private byte byte2;
    private byte byte3;

//    private short s1;
//    private char c1;

    private Demo next;

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        System.out.println("boolean:" + UnsafeKit.getObjectFieldOffset(Demo.class, "bool1"));
        System.out.println("byte:   " + UnsafeKit.getObjectFieldOffset(Demo.class, "byte1"));

//        System.out.println("short:    " + UnsafeKit.getObjectFieldOffset(Demo.class, "s1"));
//        System.out.println("char:     " + UnsafeKit.getObjectFieldOffset(Demo.class, "c1"));

        System.out.println("ref:      " + UnsafeKit.getObjectFieldOffset(Demo.class, "next"));

//        System.out.println("int:    " + UnsafeKit.getObjectFieldOffset(Demo.class, "e"));
//        System.out.println("short:  " + UnsafeKit.getObjectFieldOffset(Demo.class, "s"));
//        System.out.println("boolean:" + UnsafeKit.getObjectFieldOffset(Demo.class, "sex"));
//        System.out.println("String: " + UnsafeKit.getObjectFieldOffset(Demo.class, "str"));
//        System.out.println("String: " + UnsafeKit.getObjectFieldOffset(Demo.class, "name"));
    }

}
