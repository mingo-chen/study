package cm.study.java.core.cache;

public interface RW {

    Object load(String key);

    void store(String key, Object value);
}
