package cm.study.common.codec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Ref;

/**
 * 反射工具
 * Created by chenming on 2017/3/25.
 */
public class ReflectUtils {

    private static final Logger ILOG = LoggerFactory.getLogger( ReflectUtils.class );

    /**
     * 获取泛型类型
     * @param field
     * @param index
     * @return
     */
    public static Class<?> getGenericType(Field field, int index) {
        Type fxType = field.getGenericType();
        Type type2 = ((ParameterizedType) fxType).getActualTypeArguments()[index];
        return (Class<?>)type2;
    }

    public static boolean isSupport(Class<?> clazz) {
        if(clazz.isPrimitive()) {  // 原生类型
            return true;

        } else if(clazz == Byte.class) {
            return true;

        } else if(clazz == Short.class) {
            return true;

        } else if(clazz == Integer.class) {
            return true;

        } else if(clazz == Long.class) {
            return true;

        } else if(clazz == Float.class) {
            return true;

        } else if(clazz == Double.class) {
            return true;

        } else if(clazz == Boolean.class) {
            return true;

        } else if(clazz == Character.class) { // 原生包装类型
            return true;

        } else if(clazz ==  String.class) {
            return true;

        } else {
            return false;

        }
    }

    public static String toString(Object value) {
        if(value == null) {
            return "";
        }

        Class<?> clazz = value.getClass();
        if(clazz.isPrimitive()) {  // 原生类型
            return value.toString();

        } else if(value instanceof Byte || value instanceof Short || value instanceof Integer || value instanceof Long
                || value instanceof Float || value instanceof Double || value instanceof Boolean || value instanceof Character) { // 原生包装类型
            return value.toString();

        } else if(value instanceof String) {
            return value.toString();

        } else {
            ILOG.warn("序列化对象, type:{}, value:{}", clazz.getName(), value);
            return RedisCodec.with(value).encode().toString();

        }
    }

    public static Object fromString(String value, Class<?> type) {
        ILOG.info("value:{}, type:{}", value, type);

        if(type == int.class) {
            return Integer.parseInt(value);

        } else if(type == Integer.class) {
            return new Integer(value);

        } else if(type == long.class) {
            return Long.parseLong(value);

        } else if(type == Long.class) {
            return new Long(value);

        } else if(type == String.class) {
            return value;

        } else if(type == float.class) {
            return Float.parseFloat(value);

        } else if(type == Float.class) {
            return new Float(value);

        } else if(type == double.class) {
            return Double.parseDouble(value);

        } else if(type == Double.class) {
            return new Double(value);

        } else if(type == boolean.class) {
            return Boolean.parseBoolean(value);

        } else if(type == Boolean.class) {
            return new Boolean(value);

        } else {
            ILOG.error("未知的值类型:{}", type.getName());
            throw new CodecException("未知的值类型:" + type.getName());
//            return RedisCodec.with(value).decode(type);
        }

    }

}
