package cm.study.asm.proxy;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class ReflectUtil {

    public static Map<Class<?>, String> typeShortName = new HashMap<>();

    static {
        typeShortName.put(boolean.class, "B");
        typeShortName.put(byte.class, "B");
        typeShortName.put(short.class, "S");
        typeShortName.put(char.class, "C");
        typeShortName.put(int.class, "I");
        typeShortName.put(float.class, "F");
        typeShortName.put(long.class, "L");
        typeShortName.put(double.class, "D");
        typeShortName.put(void.class, "V");
    }

    public static String getShortName(Class<?> type) {
        String shortName = null;
        if(type.isPrimitive()) {
            shortName = typeShortName.get(type);
        } else {
            return "L" + typeToString(type);
        }

        if (shortName == null) {
            throw new RuntimeException("[" + type.getName() + "] not in type map...");
        }

        return shortName;
    }

    public static String typeToString(Class<?> type) {
        return StringUtils.replace(type.getName(), ".", "/");
    }
}
