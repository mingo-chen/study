package cm.study.java.core.memory;

import cm.study.java.core.utils.UnsafeKit;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * java对象内存布局
 */
public class ObjectMemoryLayout {

    public void getObjectSize() {
        try {
            String name = "中国";
            Field valueField = String.class.getDeclaredField("value");
            Unsafe unsafe = UnsafeKit.getUnsafe();

            Field[] fields = name.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if(Modifier.isStatic(field.getModifiers())) {
                   continue;
                }

                if(!field.getType().isArray()) {
                    System.out.println(String.format("--> %s\t%s\t%s", field.getName(), field.getType(), field.get(name)));
                    System.out.println(String.format("field: %s, type: %s, offset: %s", field.getName(), field.getType(), unsafe.objectFieldOffset(field)));
                } else {
                    System.out.println(String.format("==> %s\t%s", field.getType().getTypeName(), field.getType().getName()));

                    Class<?> arrayElementType = field.getType().getComponentType();
                    if (arrayElementType == char.class) {
                        char[] chars = (char[]) field.get(name);
                        List<Character> characterList = new ArrayList<>();
                        for (char ch : chars) {
                            characterList.add(ch);
                        }
                        System.out.println(String.format("--> %s\t%s\t%s\t%s", field.getName(), field.getType(), characterList, Arrays.toString(chars)));
                    } else {
                        System.out.println(String.format("--> %s\t%s\t%s", field.getName(), field.getType(), Arrays.asList(field.get(name))));
                    }

                    System.out.println(String.format("field: %s, type: %s, offset: %s, index: %s", field.getName(), field.getType(), unsafe.arrayBaseOffset(field.getType()), unsafe.arrayIndexScale(field.getType())));
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ObjectMemoryLayout memoryLayout = new ObjectMemoryLayout();
        memoryLayout.getObjectSize();

    }
}
