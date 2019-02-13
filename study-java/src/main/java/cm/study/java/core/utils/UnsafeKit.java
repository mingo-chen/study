package cm.study.java.core.utils;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.Arrays;

public class UnsafeKit {

    private static final Unsafe unsafe;

    static {
        try {
            Field theUnsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafeField.setAccessible(true);
            unsafe = (Unsafe) theUnsafeField.get(null);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static Unsafe getUnsafe() {
        return unsafe;
    }

    public static long getObjectFieldOffset(Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            return getUnsafe().objectFieldOffset(field);
        } catch (Exception e) {
            throw new RuntimeException("not exist field [" + fieldName + "] in " + clazz.getName());
        }
    }

    public static void main(String[] args) {
        double[] averages = new double[10];
        int rt = UnsafeKit.getUnsafe().getLoadAverage(averages, 3);
        System.out.println("--> rt: " + rt);
        System.out.println("--> averages: " + Arrays.toString(averages));

        UnsafeKit.getUnsafe().loadFence();
    }
}
