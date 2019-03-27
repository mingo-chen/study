package cm.study.java.lang.reactive.imitate;

import java.util.Comparator;
import java.util.List;

public interface Flow<T> {

    Flow<T> filter(Func1<T, Boolean> filterFunc);

    <R> Flow<R> map(Func1<T, R> mapFunc);

    <R> Flow<R> flatMap(Func1<T, Flow<R>> flatFunc);

    /**
     * 去重
     */
    Flow<T> distinct();

    /**
     * 排序
     */
    Flow<T> sorted(Comparator<? super T> comparator);

    T reduce(Func2<T, T, T> reduceFunc);

//    <K, V> List<Pair2<K, V>> reduceByKey(Func2<Pair2<K, V>, Pair2<K, V>, Pair2<K, V>> reduceFunc);
    <K, V> List<Pair2<K, V>> reduceByKey(Func1<Pair2, K> keyExtractor, Func1<Pair2, V> valueExtractor, Func2<V, V, V> reduceFunc);

    List<T> collection();

    /**
     * 最小值
     */
    T min(Comparator<? super T> comparator);

    T max(Comparator<? super T> comparator);

    T first();

    long count();

    void forEach(Func0<T> forFunc);

}
