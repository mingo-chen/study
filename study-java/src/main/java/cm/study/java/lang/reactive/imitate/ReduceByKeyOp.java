package cm.study.java.lang.reactive.imitate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReduceByKeyOp<P, K, V> implements Sink<P> {

    private Func1<P, K> keyExtractor = null;
    private Func1<P, V> valueExtractor = null;
    private Func2<V, V, V> reduceFunc = null;

    private Map<K, V> result = new HashMap<>();

    public ReduceByKeyOp(Func1<P, K> keyExtractor, Func1<P, V> valueExtractor, Func2<V, V, V> reduceFunc) {
        this.keyExtractor = keyExtractor;
        this.valueExtractor = valueExtractor;
        this.reduceFunc = reduceFunc;
    }

    @Override
    public void accept(P each) {
        K key = keyExtractor.apply(each);
        V old = result.get(key);
        if (old == null) {
            result.put(key, valueExtractor.apply(each));
        } else {
            V newV = reduceFunc.apply(old, valueExtractor.apply(each));
            result.put(key, newV);
        }
    }

    public List<Pair2<K, V>> output() {
        List<Pair2<K, V>> list = new ArrayList<>();

        for (Map.Entry<K, V> entry : result.entrySet()) {
            list.add(new Pair2<>(entry.getKey(), entry.getValue()));
        }

        return list;
    }

}
