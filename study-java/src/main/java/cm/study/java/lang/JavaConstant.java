package cm.study.java.lang;

public class JavaConstant {

    public static void main(String[] args) {
        intDemo();

        stringDemo();
    }

    public static void intDemo() {
        Integer low1 = -129;
        Integer low2 = -129;
        System.out.println("---> low equals: " + (low1 == low2));

        Integer height1 = 333;
        Integer height2 = 333;
        System.out.println("---> height equals: " + (height1 == height2));
    }

    public static void stringDemo() {
        String str1 = "abc";
        String str2 = new String("abc");

        System.out.println(str1 == str2);
        System.out.println(str1 == str2.intern());
    }
}
