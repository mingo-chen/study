package cm.study.asm.common;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.objectweb.asm.Opcodes;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ReflectUtil {

    public static Map<Class<?>, String> typeShortName = new HashMap<>();
    public static Map<Class<?>, Integer> typeLoadCode = new HashMap<>();
    public static Map<Class<?>, Integer> typeStoreCode = new HashMap<>();
    public static Map<Class<?>, Integer> typeReturnCode = new HashMap<>();
    public static Map<Class<?>, Method> primitiveUnwrapMethod = new HashMap<>();
    public static Map<Class<?>, Method> primitiveWrapMethod = new HashMap<>();
    public static Map<Class<?>, Class<?>> primitive2WrapType = new HashMap<>();

    static {
        typeShortName.put(boolean.class, "Z");
        typeShortName.put(byte.class, "B");
        typeShortName.put(short.class, "S");
        typeShortName.put(char.class, "C");
        typeShortName.put(int.class, "I");
        typeShortName.put(float.class, "F");
        typeShortName.put(long.class, "L");
        typeShortName.put(double.class, "D");
        typeShortName.put(void.class, "V");

        typeLoadCode.put(boolean.class, Opcodes.ILOAD);
        typeLoadCode.put(byte.class, Opcodes.ILOAD);
        typeLoadCode.put(short.class, Opcodes.ILOAD);
        typeLoadCode.put(int.class, Opcodes.ILOAD);
        typeLoadCode.put(long.class, Opcodes.LLOAD);
        typeLoadCode.put(float.class, Opcodes.FLOAD);
        typeLoadCode.put(double.class, Opcodes.DLOAD);

        typeStoreCode.put(boolean.class, Opcodes.ISTORE);
        typeStoreCode.put(byte.class, Opcodes.ISTORE);
        typeStoreCode.put(short.class, Opcodes.ISTORE);
        typeStoreCode.put(int.class, Opcodes.ISTORE);
        typeStoreCode.put(long.class, Opcodes.LSTORE);
        typeStoreCode.put(float.class, Opcodes.FSTORE);
        typeStoreCode.put(double.class, Opcodes.DSTORE);

        typeReturnCode.put(boolean.class, Opcodes.IRETURN);
        typeReturnCode.put(byte.class, Opcodes.IRETURN);
        typeReturnCode.put(short.class, Opcodes.IRETURN);
        typeReturnCode.put(int.class, Opcodes.IRETURN);
        typeReturnCode.put(long.class, Opcodes.LRETURN);
        typeReturnCode.put(float.class, Opcodes.FRETURN);
        typeReturnCode.put(double.class, Opcodes.DRETURN);

        try {
            primitive2WrapType.put(boolean.class, Boolean.class);
            primitive2WrapType.put(byte.class, Byte.class);
            primitive2WrapType.put(short.class, Short.class);
            primitive2WrapType.put(int.class, Integer.class);
            primitive2WrapType.put(long.class, Long.class);
            primitive2WrapType.put(float.class, Float.class);
            primitive2WrapType.put(double.class, Double.class);

            primitiveWrapMethod.put(boolean.class, Boolean.class.getMethod("valueOf", boolean.class));
            primitiveWrapMethod.put(byte.class, Byte.class.getMethod("valueOf", byte.class));
            primitiveWrapMethod.put(short.class, Short.class.getMethod("valueOf", short.class));
            primitiveWrapMethod.put(int.class, Integer.class.getMethod("valueOf", int.class));
            primitiveWrapMethod.put(long.class, Long.class.getMethod("valueOf", long.class));
            primitiveWrapMethod.put(float.class, Float.class.getMethod("valueOf", float.class));
            primitiveWrapMethod.put(double.class, Double.class.getMethod("valueOf", double.class));

            primitiveUnwrapMethod.put(boolean.class, Boolean.class.getMethod("booleanValue"));
            primitiveUnwrapMethod.put(byte.class, Byte.class.getMethod("byteValue"));
            primitiveUnwrapMethod.put(short.class, Short.class.getMethod("shortValue"));
            primitiveUnwrapMethod.put(int.class, Integer.class.getMethod("intValue"));
            primitiveUnwrapMethod.put(long.class, Long.class.getMethod("longValue"));
            primitiveUnwrapMethod.put(float.class, Float.class.getMethod("floatValue"));
            primitiveUnwrapMethod.put(double.class, Double.class.getMethod("doubleValue"));
        } catch (Exception e) {

        }
    }

    public static String getShortName(Class<?> type) {
        String shortName = null;
        if(type.isPrimitive()) {
            shortName = typeShortName.get(type);
        } else {
            return "L" + typeToString(type)+";";
        }

        if (shortName == null) {
            throw new RuntimeException("[" + type.getName() + "] not in type map...");
        }

        return shortName;
    }

    public static String typeToString(Class<?> type) {
        if(type.isPrimitive()) {
            return typeShortName.get(type);
        } else {
            return StringUtils.replace(type.getName(), ".", "/");
        }
    }

    public static Object getValue(String value, Class<?> type) {
        if (type == int.class) {
            return NumberUtils.toInt(value);
        } else if (type == String.class) {
            return value;
        } else {
            throw new RuntimeException("[" + type.getName() + "] not support...");
        }
    }

    public static int getLoadFlag(Class<?> type) {
        if(type.isPrimitive()) {
            return typeLoadCode.get(type);

        } else {
            return Opcodes.ALOAD;
        }
    }

    public static int getStoreFlag(Class<?> type) {
        if(type.isPrimitive()) {
            return typeStoreCode.get(type);

        } else {
            return Opcodes.ASTORE;
        }
    }

    public static int getReturnFlag(Class<?> type) {
        if(type.isPrimitive()) {
            return typeReturnCode.get(type);

        } else {
            return Opcodes.ARETURN;
        }
    }

    public static Method getUnwrapMethod(Class<?> primitive) {
        return primitiveUnwrapMethod.get(primitive);
    }

    public static Class<?> getWrapType(Class<?> primitive) {
        Class<?> wrapType = primitive2WrapType.get(primitive);
        if (wrapType == null) {
            throw new RuntimeException("[" + primitive.getName() + "] not config...");
        }

        return wrapType;
    }

    public static Method getWrapMethod(Class<?> primitive) {
        return primitiveWrapMethod.get(primitive);
    }
}
