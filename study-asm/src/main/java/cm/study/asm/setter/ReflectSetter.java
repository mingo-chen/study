package cm.study.asm.setter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Map;

public class ReflectSetter<T> implements Setter<T> {

    private static Logger ILOG = LoggerFactory.getLogger(ReflectSetter.class);

    @Override
    public void payload(T carrier, Map<String, Object> data) {
        Map<String, Class<?>> fieldTypes = Reflects.getFieldTypes(carrier.getClass());

        for (Map.Entry<String, Class<?>> entry : fieldTypes.entrySet()) {
            reflectSet(carrier, entry.getKey(), data.get(entry.getKey()));
        }

    }

    @Override
    public void rawPayload(T carrier, Map<String, String> data) {
        Map<String, Class<?>> fieldTypes = Reflects.getFieldTypes(carrier.getClass());

        for (Map.Entry<String, Class<?>> entry : fieldTypes.entrySet()) {
            String rawValue = data.get(entry.getKey());
            Object value = Reflects.to(rawValue, entry.getValue());
            reflectSet(carrier, entry.getKey(), value);
        }

    }

    void reflectSet(Object target, String key, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(key);
            if (field == null) {
                //
                ILOG.warn("property[{}] not exist!", key);
            } else {
                field.setAccessible(true);
                field.set(target, value);
            }

        } catch (Exception e) {
            ILOG.error("reflect set error, key: {}, value: {}", key, value, e);
        }
    }

}
