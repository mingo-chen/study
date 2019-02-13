package cm.study.java.core.concurrent;

import cm.study.java.core.utils.UnsafeKit;

public class PutAndGet {

    private static long nameOffset;

    private static long ageOffset;

    static {
        try {
            nameOffset = UnsafeKit.getUnsafe().objectFieldOffset(Person.class.getDeclaredField("name"));
            ageOffset = UnsafeKit.getUnsafe().objectFieldOffset(Person.class.getDeclaredField("age"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Person person = new Person();
        UnsafeKit.getUnsafe().putObject(person, nameOffset, "cm");
        UnsafeKit.getUnsafe().putInt(person, ageOffset, 18);
        System.out.println(person.getName());
        System.out.println(person.getAge());
    }
}
