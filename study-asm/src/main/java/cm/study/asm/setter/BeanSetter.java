package cm.study.asm.setter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class BeanSetter {

    private static Logger ILOG = LoggerFactory.getLogger(BeanSetter.class);

    public static <T> void payload(T carrier, Map<String, Object> data) {
        try {
            new AsmSetter<T>().payload(carrier, data);
        } catch (Exception e) {
            ILOG.error("asm set bean value error, carrier: {}, data: {}", carrier, data, e);
            new ReflectSetter<T>().payload(carrier, data);
        }
    }

    public static <T> void rawPayload(T carrier, Map<String, String> data) {
        try {
            new AsmSetter<T>().rawPayload(carrier, data);
        } catch (Exception e) {
            ILOG.error("asm raw set bean value error, carrier: {}, data: {}", carrier, data, e);
            new ReflectSetter<T>().rawPayload(carrier, data);
        }
    }
}
