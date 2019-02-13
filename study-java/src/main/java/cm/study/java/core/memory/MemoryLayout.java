package cm.study.java.core.memory;

import cm.study.java.core.utils.UnsafeKit;

import java.lang.reflect.Field;

public class MemoryLayout {
    private char[] options = new char[12];
    private String name;
    private char age;
    private short sex;
    private long money;

    public static void main(String[] args) {
        long ageOffset = getFieldOffset("age");
        long sexOffset = getFieldOffset("sex");
        long moneyOffset = getFieldOffset("money");
        long nameOffset = getFieldOffset("name");
        long optionsOffset = getFieldOffset("options");

        System.out.println("--> " + moneyOffset);
        System.out.println("--> " + ageOffset);
        System.out.println("--> " + sexOffset);
        System.out.println("--> " + optionsOffset);
        System.out.println("--> " + nameOffset);
    }

    public static long getFieldOffset(String fieldName) {
        try {
            Field field = MemoryLayout.class.getDeclaredField(fieldName);
            return UnsafeKit.getUnsafe().objectFieldOffset(field);
        } catch (Exception e) {
            throw new RuntimeException("not exist field [" + fieldName + "] in MemoryLayout");
        }
    }
}
