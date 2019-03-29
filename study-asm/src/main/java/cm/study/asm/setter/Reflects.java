package cm.study.asm.setter;

import cm.study.asm.common.DateUtil;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class Reflects {

    /**
     * 目前支持的类型有:
     * 1) 基本类型及对应的包装类型
     * 2) String
     * 3) java.util.Date
     */
    public static <T> T to(String raw, Class<T> type) {
        if (raw == null) {
            return null;
        }

        if (type == int.class || type == Integer.class) {
            return (T) Integer.valueOf(raw);

        } else if (type == byte.class || type == Byte.class) {
            return (T) Byte.valueOf(raw);

        } else if (type == boolean.class || type == Boolean.class) {
            return (T) Boolean.valueOf(raw);

        } else if (type == char.class || type == Character.class) {
            char[] chs = raw.toCharArray(); // 可能为空
            if(chs.length == 0) {
                return null;
            } else {
                return (T) Character.valueOf(chs[0]);
            }

        } else if (type == short.class || type == Short.class) {
            return (T) Short.valueOf(raw);

        } else if (type == long.class || type == Long.class) {
            return (T) Long.valueOf(raw);

        } else if(type == float.class || type == Float.class) {
            return (T) Float.valueOf(raw);

        } else if (type == double.class || type == Double.class) {
            return (T) Double.valueOf(raw);

        } else if(type == String.class) {
            return (T) raw;

        } else if (type == Date.class) {
            return (T) DateUtil.fromString(raw, DateUtil.YYYYMMDDHHmmss);
        }

        throw new RuntimeException("unknown support type [" + type + "]");
    }

    public static Map<String, Class<?>> getFieldTypes(Class<?> type) {
        Field[] fields = type.getDeclaredFields();

        Map<String, Class<?>> result = new LinkedHashMap<>();
        for (Field field : fields) {
            result.put(field.getName(), field.getType());
        }

        return result;
    }

    public static String genSetterMethodName(String fieldName) {
        StringBuilder sb = new StringBuilder("set");
        char firstChar = fieldName.charAt(0);
        if(firstChar >= 'a' && firstChar <= 'z') {
            // 首字母变大小, 其它不变
            sb.append((char) (firstChar - 32)).append(fieldName.substring(1));

        } else {
            sb.append(fieldName);
        }

        return sb.toString();
    }
}
