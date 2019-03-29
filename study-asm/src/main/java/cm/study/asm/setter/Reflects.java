package cm.study.asm.setter;

import cm.study.asm.common.DateUtil;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Reflects {

    public static <T> T to(String raw, Class<T> type) {
        if (raw == null) {
            return null;
        }

        if(type.isPrimitive()) {
            if (type == int.class) {
                return (T) Integer.valueOf(raw);

            } else if (type == boolean.class) {
                return (T) Boolean.valueOf(raw);

            } else if (type == long.class || type == Long.class) {
                return (T) Long.valueOf(raw);

            } else if (type == double.class || type == Double.class) {
                return (T) Double.valueOf(raw);

            }
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
}
