package cm.study.java.lang.reactive.imitate;

import java.util.*;

/**
 * 使用List存储数据
 * 每一个转换会生成一个新的Flow对象
 */
public class FlowV1<T> implements Flow<T> {

    private List<T> source;

    FlowV1(List<T> source) {
        this.source = source;
    }

    FlowV1(T... ts) {
        source = new ArrayList<>();

        for (T t : ts) {
            source.add(t);
        }
    }

    @Override
    public Flow<T> filter(Func1<T, Boolean> filterFunc) {
        List<T> after = new ArrayList<>();
        for (T t : source) {
            if(filterFunc.apply(t))
                after.add(t);
        }

        return new FlowV1<>(after);
    }

    @Override
    public <R> Flow<R> map(Func1<T, R> mapFunc) {
        List<R> after = new ArrayList<>();
        for (T t : source) {
            after.add(mapFunc.apply(t));
        }

        return new FlowV1<>(after);
    }

    @Override
    public <R> Flow<R> flatMap(Func1<T, Flow<R>> flatFunc) {
        List<R> after = new ArrayList<>();

        for (T t : source) {
            Flow<R> flow = flatFunc.apply(t);

            // flow add to after
            after.addAll(((FlowV1) flow).source);
        }

        return new FlowV1<>(after);
    }

    @Override
    public T reduce(Func2<T, T, T> reduceFunc) {
        T init = null;

        for (T t : source) {
            if (init == null) {
                init = t;
            } else {
                init = reduceFunc.apply(init, t);
            }
        }

        return init;
    }

    @Override
    public <K, V> List<Pair2<K, V>> reduceByKey(Func1<Pair2, K> keyExtractor, Func1<Pair2, V> valueExtractor, Func2<V, V, V> reduceFunc) {
        Map<K, V> result = new HashMap<>();

        for (T t : source) {
            if(t instanceof Pair2) {
                K key = keyExtractor.apply((Pair2<K, V>) t);
                V value = valueExtractor.apply((Pair2) t);

                if (result.containsKey(key)) {
                    result.put(key, reduceFunc.apply(value, result.get(key)));
                } else {
                    result.put(key, value);
                }
            } else {
                throw new RuntimeException("before reduce by key, please build to pair2....");
            }
        }

        List<Pair2<K, V>> pair2s = new ArrayList<>();
        for (Map.Entry<K, V> entry : result.entrySet()) {
            pair2s.add(new Pair2<>(entry.getKey(), entry.getValue()));
        }

        return pair2s;
    }

    @Override
    public Flow<T> distinct() {
        List<T> after = new ArrayList<>();
        Set<T> set = new HashSet<>();
        for (T t : source) {
            if(!set.contains(t)) {
                after.add(t);
            }
            set.add(t);
        }

        return new FlowV1<>(after);
    }

    @Override
    public Flow<T> sorted(Comparator<? super T> comparator) {
        List<T> after = new ArrayList<>(source);
        Collections.sort(after, comparator);

        return new FlowV1<>(after);
    }

    @Override
    public List<T> collection() {
        return source;
    }

    @Override
    public T min(Comparator<? super T> comparator) {
        T min = null;

        for (T t : source) {
            if (min == null) {
                min = t;
            } else {
                min = comparator.compare(min, t) <= 0 ? min : t;
            }
        }

        return min;
    }

    @Override
    public T max(Comparator<? super T> comparator) {
        T max = null;

        for (T t : source) {
            if (max == null) {
                max = t;
            } else {
                max = comparator.compare(max, t) < 0 ? t : max;
            }
        }

        return max;
    }

    @Override
    public T first() {
        return source == null || source.isEmpty() ? null : source.get(0);
    }

    @Override
    public long count() {
        return source.size();
    }

    @Override
    public void forEach(Func0<T> forFunc) {
        for (T t : source) {
            forFunc.apply(t);
        }
    }

    @Override
    public String toString() {
        return "FlowV1{" +
               "source=" + source +
               '}';
    }
}
