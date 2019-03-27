package cm.study.java.lang.reactive.imitate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 参考spark的实现, 把操作分为2类,
 * 一类是: transform, 转换, 可以先不执行, 输出还是一个Flow
 * 一类是: action, 动作, 需要输出结果, 输出是其它类型
 * @param <T>
 */
public class FlowV2<T> implements Flow<T> {

    private Sink<T> sink;

    private FlowPipeline<T, T> initPipe;

//    FlowV2(List<T> source) {
//        sink = SinkImpl.with(source);
//    }
//
//    FlowV2(T... ts) {
//        sink = SinkImpl.of(ts);
//    }

    @Override
    public Flow<T> filter(Func1<T, Boolean> filterFunc) {
        initPipe = (FlowPipeline)initPipe.filter(filterFunc);
        return this;
    }

    @Override
    public <R> Flow<R> map(Func1<T, R> mapFunc) {
        return null;
    }

    @Override
    public <R> Flow<R> flatMap(Func1<T, Flow<R>> flatFunc) {
        return null;
    }

    @Override
    public T reduce(Func2<T, T, T> reduceFunc) {
        return null;
    }

    @Override
    public <K, V> List<Pair2<K, V>> reduceByKey(Func1<Pair2, K> keyExtractor, Func1<Pair2, V> valueExtractor, Func2<V, V, V> reduceFunc) {
        return null;
    }

    @Override
    public Flow<T> distinct() {
        return this;
    }

    @Override
    public Flow<T> sorted(Comparator<? super T> comparator) {
        return this;
    }

    @Override
    public List<T> collection() {
        return null;
    }

    @Override
    public T min(Comparator<? super T> comparator) {
        return null;
    }

    @Override
    public T max(Comparator<? super T> comparator) {
        return null;
    }

    @Override
    public T first() {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void forEach(Func0<T> forFunc) {

    }
}
