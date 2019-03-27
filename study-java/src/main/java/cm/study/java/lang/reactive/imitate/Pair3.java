package cm.study.java.lang.reactive.imitate;

public class Pair3<K, T, V> {

    private K key;

    private T tag;

    private V value;

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public T getTag() {
        return tag;
    }

    public void setTag(T tag) {
        this.tag = tag;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Pair3{" +
               "key=" + key +
               ", tag=" + tag +
               ", value=" + value +
               '}';
    }
}
