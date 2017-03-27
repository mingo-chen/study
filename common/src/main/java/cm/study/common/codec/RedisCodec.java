package cm.study.common.codec;

import com.sun.deploy.util.ReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.sql.Ref;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * @return
     */
    public String encode() throws CodecException {
        ILOG.info("目标对象:{}", target);

        if (this.target == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        try {
            // 考虑父类继承的字段
            Field[] fields = target.getClass().getDeclaredFields();
            for (Field field : fields) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(target);

                    Class<?> fieldType = field.getType();

                    if(fieldType.isArray()) {
                        Object[] arrays = (Object[]) value;
                        StringBuilder subSb = new StringBuilder();
                        for(Object v : arrays) {
                            String s_value = ReflectUtils.toString(v);
                            subSb.append(s_value.length()).append(",").append(s_value);
                        }
                        sb.append(subSb.length()).append(",").append(subSb);

                    } else if(fieldType == List.class) {
                        List lists = (List) value;
                        StringBuilder subSb = new StringBuilder();
                        for(Object v : lists) {
                            String s_value = ReflectUtils.toString(v);
                            subSb.append(s_value.length()).append(",").append(s_value);
                        }
                        sb.append(subSb.length()).append(",").append(subSb);

                    } else if(fieldType == Map.class) {
                        Map map = (Map) value;
                        StringBuilder subSb = new StringBuilder();
                        for(Object k : map.keySet()) {
                            Object v = map.get(k);
                            String s_key = ReflectUtils.toString(k);
                            String s_value = ReflectUtils.toString(v);

                            subSb.append(s_key.length()).append(",").append(s_key);
                            subSb.append(s_value.length()).append(",").append(s_value);
                        }

                        sb.append(subSb.length()).append(",").append(subSb);

                    } else {
                        String s_value = ReflectUtils.toString(value);
                        sb.append(s_value.length()).append(",").append(s_value);
                    }

                } catch (IllegalAccessException iae) {
                    throw new CodecException(iae);
                }
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
    public <T> T decode(Class<T> clazz) {
        T result = null;

        try {
            result = clazz.newInstance();

            List<String> valuePairs = splitTarget((String) target);

            Field[] fields = clazz.getDeclaredFields();
            if(fields.length == 0) { // 没有字段
                return (T) getOriginalText(valuePairs.get(0));

            } else {
                for(int index = 0; index < fields.length; index++) {
                    Field field = fields[index];
                    field.setAccessible(true);

                    String valuePair = valuePairs.get(index);
                    int splitIndex = valuePair.indexOf(",");
                    String s_fieldValue = valuePair.substring(splitIndex + 1);

                    Class<?> fieldType = field.getType();

                    if (fieldType.isArray()) {
                        Class<?> fxType = fieldType.getComponentType();
                        List<String> arrayValues = splitTarget(s_fieldValue);

                        Object convertValues = Array.newInstance(fxType, arrayValues.size());
                        for (int idx = 0; idx < arrayValues.size(); idx++) {
                            String originValue = getOriginalText(arrayValues.get(idx));
                            Object value = ReflectUtils.fromString(originValue, fxType);
                            Array.set(convertValues, idx, value);
                        }
                        field.set(result, convertValues);

                    } else if (fieldType == List.class) {
                        Class<?> fxType = ReflectUtils.getGenericType(field, 0);
                        List<String> arrayValues = splitTarget(s_fieldValue);

                        List convertValues = new ArrayList();
                        for (int idx = 0; idx < arrayValues.size(); idx++) {
                            String originValue = getOriginalText(arrayValues.get(idx));
                            Object value = ReflectUtils.fromString(originValue, fxType);
                            convertValues.add(value);
                        }
                        field.set(result, convertValues);

                    } else if (fieldType == Map.class) {
                        List<String> arrayValues = splitTarget(s_fieldValue);

                        Class<?> keyType = ReflectUtils.getGenericType(field, 0);
                        Class<?> valueType = ReflectUtils.getGenericType(field, 1);

                        Map convertValues = new HashMap();
                        for (int idx = 0; idx < arrayValues.size(); idx += 2) {
                            String originalKeyText = getOriginalText(arrayValues.get(idx));
                            String originalValueText = getOriginalText(arrayValues.get(idx + 1));

                            Object key = ReflectUtils.fromString(originalKeyText, keyType);
                            Object value = ReflectUtils.isSupport(valueType) ? ReflectUtils.fromString(originalValueText, valueType) :
                                    (RedisCodec.with(arrayValues.get(idx + 1)).decode(valueType));
                            convertValues.put(key, value);
                        }
                        field.set(result, convertValues);

                    } else {
//                        Object value = ReflectUtils.fromString(s_fieldValue, fieldType);
                        Object value = ReflectUtils.isSupport(fieldType) ? ReflectUtils.fromString(s_fieldValue, fieldType) :
                                (RedisCodec.with(s_fieldValue).decode(fieldType));
                        field.set(result, value);
                    }
                }
            }

            return result;

        } catch (Exception e) {
            ILOG.error("反编码错误, clazz:" + clazz.getName() + ", target:" + target, e);
            ILOG.error("result:{}", result);
            return null;
        }

    }

    List<String> splitTarget(String text) {
//        = (String) target;

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
