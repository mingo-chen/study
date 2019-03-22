package cm.study.asm.common;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.objectweb.asm.Opcodes;

import java.util.HashMap;
import java.util.Map;

public class ReflectUtil {

    public static Map<Class<?>, String> typeShortName = new HashMap<>();
    public static Map<Class<?>, Integer> typeLoadCode = new HashMap<>();
    public static Map<Class<?>, Integer> typeStoreCode = new HashMap<>();
    public static Map<Class<?>, Integer> typeReturnCode = new HashMap<>();

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
        return StringUtils.replace(type.getName(), ".", "/");
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
}
