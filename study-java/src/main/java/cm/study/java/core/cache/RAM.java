package cm.study.java.core.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 主存
 */
public class RAM implements RW {

    private static Logger ILOG = LoggerFactory.getLogger(RAM.class);

    Map<String, Object> store = new HashMap<>();

    @Override
    public Object load(String key) {
        return store.get(key);
    }

    @Override
    public void store(String key, Object value) {
        store.put(key, value);
        ILOG.debug("ram write new value, key: {}, value: {}", key, value);
    }

    @Override
    public String toString() {
        return "RAM{" +
               "store=" + store +
               '}';
    }
}
