package cm.study.common.codec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Redis编码
 * len1,value1len2,value2len3,value3...
 * 如果value是基本类型 则直接toString
 * 如果value是Array或Collection, 则以,分隔
 * 如果value是Map类型 则k1:v2,k2:v2,k3:v3...
 *
 * 如果value是Object, 则递归上述步骤
 * Created by chenming on 2017/3/25.
 */
public class RedisCodec {

    private static final Logger ILOG = LoggerFactory.getLogger( RedisCodec.class );

    private Object target;

    public static RedisCodec with(Object target) {
        RedisCodec redisCodec = new RedisCodec();
        redisCodec.target = target;
        return redisCodec;
    }

    /**
     * 把对象按redis编码进行编码
     * 1) 如果是基本类型,则直接len(obj),obj.toString();
     * 2) 如果是Array, len(...),len(ele1),ele1len(ele2),ele2...
     * 3) 如果是Collection, len(...),len(ele1),ele1len(ele2),ele2...
     * 4) 如果是Map, len(...),len(k1),k1len(v1),v1len(k2),k2len(v2),v2...
     *
     * 5) 如果是Bean, 则用反射获取Bean的每个Field, 然后根据Field的类型, 再走1~4步
     * @return
     */
    public String encode() throws CodecException {
        ILOG.info("目标对象:{}", target);

        if (this.target == null) {
            return "0,"; // 按位置取的, 不能为空
        }

        StringBuilder sb = new StringBuilder();

        try {
            Class<?> targetType = target.getClass();

            if(ReflectUtils.isPrimitive(targetType)) { // 基本类型
                String text = ReflectUtils.toString(target);
                sb.append(text.length()).append(",").append(text);

            } else if (targetType.isArray()) { // 数组类型
                Object[] arrays = (Object[]) target;
                StringBuilder subSb = new StringBuilder();
                for(Object v : arrays) {
                    String s_value = RedisCodec.with(v).encode();
                    subSb.append(s_value);
                }
                sb.append(subSb.length()).append(",").append(subSb);

            } else if(Collection.class.isAssignableFrom(targetType)) { // 集合
                Collection lists = (Collection) target;
                StringBuilder subSb = new StringBuilder();
                for(Object v : lists) {
                    String s_value = RedisCodec.with(v).encode();
                    subSb.append(s_value);
                }
                sb.append(subSb.length()).append(",").append(subSb);

            } else if(Map.class.isAssignableFrom(targetType)) { // Map
                Map map = (Map) target;
                StringBuilder subSb = new StringBuilder();
                for(Object k : map.keySet()) {
                    Object v = map.get(k);

                    String s_key = RedisCodec.with(k).encode();
                    String s_value = RedisCodec.with(v).encode();

                    subSb.append(s_key);
                    subSb.append(s_value);
                }

                sb.append(subSb.length()).append(",").append(subSb);

            } else { // Object Bean
                // 考虑父类继承的字段
                StringBuilder subSb = new StringBuilder();
                Field[] fields = target.getClass().getDeclaredFields();
                for (Field field : fields) {
                    try {
                        field.setAccessible(true);
                        Object fieldValue = field.get(target);

                        String s_value = RedisCodec.with(fieldValue).encode();
                        subSb.append(s_value);

                    } catch (IllegalAccessException iae) {
                        throw new CodecException(iae);
                    }
                }
                sb.append(subSb.length()).append(",").append(subSb);
            }
        } catch (Exception e) {
            ILOG.error("reflect error", e);
        }

        return sb.toString();
    }

    /**
     * 把对象按redis编码进行解码
     * @return
     */
    public <T> T decode(List<Class<?>> clazzss) {
        if(clazzss == null || clazzss.isEmpty()) {
            throw new RuntimeException("值类型为空");
        }

        T result = null;
        Class<T> clazz = (Class<T>) clazzss.get(0);

        try {
            String text = getOriginalText((String)target);

            if(ReflectUtils.isPrimitive(clazz)) { // 基本类型
                // 基本类型
                return (T) ReflectUtils.fromString(text, clazz);

            } else if(clazz.isArray()) { // 数组类型
                Class<?> fxType = clazz.getComponentType();
                List<String> arrayValues = splitTarget(text);

                Object convertValues = Array.newInstance(fxType, arrayValues.size());
                for (int idx = 0; idx < arrayValues.size(); idx++) {
                    Object value = RedisCodec.with(arrayValues.get(idx)).decode(Arrays.asList(fxType));
                    Array.set(convertValues, idx, value);
                }
                return (T) convertValues;

            } else if(Collection.class.isAssignableFrom(clazz)) { // 集合
                Class<?> fxType = clazzss.size()>1? clazzss.get(1) : Object.class;
                return RedisCodec.with(target).decodeCollection(clazz, fxType);

            } else if(Map.class.isAssignableFrom(clazz)) { // Map
                Class<?> keyType = clazzss.size()>1? clazzss.get(1) : Object.class;
                Class<?> valueType = clazzss.size()>2? clazzss.get(2) : Object.class;

                return RedisCodec.with(target).decodeMap(clazz, keyType, valueType);

            } else { // Object Bean
                result = clazz.newInstance();
                Field[] fields = clazz.getDeclaredFields();
                if(fields.length == 0) { // 没有字段
                    ILOG.error("对象没有字段: class:{}, target:{}", clazz.getName(), target);
                    return (T) getOriginalText((String) target);

                } else {
                    List<String> valuePairs = splitTarget(getOriginalText((String) target));
                    for (int index = 0; index < fields.length; index++) {
                        Field field = fields[index];
                        field.setAccessible(true);

                        String valuePair = valuePairs.get(index);

                        Class<?> fieldType = field.getType();
                        Object value = null;
                        if(Collection.class.isAssignableFrom(fieldType)) {
                            value = RedisCodec.with(valuePair).decode(Arrays.asList(fieldType, ReflectUtils.getGenericType(field, 0)));

                        } else if(Map.class.isAssignableFrom(fieldType)) {
                            Class<?> keyType = ReflectUtils.getGenericType(field, 0);
                            Class<?> valueType = ReflectUtils.getGenericType(field, 1);

                            value = RedisCodec.with(valuePair).decode(Arrays.asList(fieldType, keyType, valueType));

                        } else {
                            value = RedisCodec.with(valuePair).decode(Arrays.asList(fieldType));
                        }

                        field.set(result, value);
                    }
                }

                return result;
            }

        } catch (Exception e) {
            ILOG.error("反编码错误, clazz:" + clazz.getName() + ", target:" + target, e);
            ILOG.error("result:{}", result);
            return null;
        }

    }

    public <T> T decodeCollection(Class<?> collectionType, Class<?> elementType) {
        List<String> arrayValues = splitTarget(getOriginalText((String)target));

        List convertValues = new ArrayList();
        for (int idx = 0; idx < arrayValues.size(); idx++) {
            Object value = RedisCodec.with(arrayValues.get(idx)).decode(Arrays.asList(elementType));
            convertValues.add(value);
        }
        return (T) convertValues;
    }

    public <T> T decodeMap(Class<?> mapType, Class<?> keyType, Class<?> valueType) {
        List<String> arrayValues = splitTarget(getOriginalText((String)target));

        Map convertValues = new HashMap();
        for (int idx = 0; idx < arrayValues.size(); idx += 2) {
            Object key = RedisCodec.with(arrayValues.get(idx)).decode(Arrays.asList(keyType));
            Object value = RedisCodec.with(arrayValues.get(idx + 1)).decode(Arrays.asList(valueType));
            convertValues.put(key, value);
        }
        return (T) convertValues;
    }


    List<String> splitTarget(String text) {
        int cursor = 0;
        List<String> result = new ArrayList<String>();
        for(;;) {
            int lenIndex = text.indexOf(",", cursor);
            int len = Integer.parseInt(text.substring(cursor, lenIndex));
            int endIndex = cursor + ("" + len).length() + 1 + len;

            result.add(text.substring(cursor, endIndex));
            cursor = endIndex;

            if(endIndex >= text.length()) {
                break;
            }
        }

        return result;
    }

    String getOriginalText(String encodeText) {
        int lenIndex = encodeText.indexOf(",", 0);
        int len = Integer.parseInt(encodeText.substring(0, lenIndex));
        int startIndex = ("" + len).length() + 1;
        int endIndex = ("" + len).length() + 1 + len;
        return encodeText.substring(startIndex, endIndex);
    }

}
