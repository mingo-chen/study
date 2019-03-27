package cm.study.java.lang.reactive.imitate;

public class Pair2<K, V> {

    private K key;

    private V value;

    public Pair2() {
    }

    public Pair2(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Pair2{" +
               "key=" + key +
               ", value=" + value +
               '}';
    }
}
