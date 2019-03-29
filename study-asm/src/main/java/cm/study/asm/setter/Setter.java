package cm.study.asm.setter;

import java.util.Map;

public interface Setter<T> {

    void payload(T carrier, Map<String, Object> data);

    void rawPayload(T carrier, Map<String, String> data);
}
