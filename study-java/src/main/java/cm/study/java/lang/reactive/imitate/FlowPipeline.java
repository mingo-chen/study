package cm.study.java.lang.reactive.imitate;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * 代表对数据的操作过程
 */
public abstract class FlowPipeline<B, T> implements Flow<T> {

    protected List<T> data;

    private FlowPipeline<?, ?> prevStage;

    /**
     * Pipeline创建方法, 只有数据流, 没有prevStage
     * @param data
     */
    public FlowPipeline(List<T> data) {
        this.data = data;
        prevStage = null;
    }

    /**
     * 每次对前一个Pipeline进行转换/包装, 生成新的Pipeline,
     * 底层的sink还未改变, 直到调用转换/包装方法
     */
    public FlowPipeline(FlowPipeline<?, ?> prevStage) {
        this.prevStage = prevStage;
    }

    /**
     * 对Sink进行包装
     * 注意参数的泛型
     */
    public abstract Sink<B> onWrapSink(Sink<T> sink);

    @Override
    public Flow<T> filter(Func1<T, Boolean> filterFunc) {

        return new FlowPipeline<T, T>(this) {
            @Override
            public Sink<T> onWrapSink(Sink<T> stream) {
                return new SinkImpl<T, T>(stream) {
                    @Override
                    public void accept(T each) {
                        if(filterFunc.apply(each)) {
                            downstream.accept(each);
                        }
                    }
                };
            }
        };
    }

    @Override
    public <R> Flow<R> map(Func1<T, R> mapFunc) {
        return new FlowPipeline<T, R>(this) {
            @Override
            public Sink<T> onWrapSink(Sink<R> stream) {
                return new SinkImpl<T, R>(stream) {

                    @Override
                    public void accept(T each) {
                        downstream.accept(mapFunc.apply(each));
                    }
                };
            }
        };

    }

    @Override
    public <R> Flow<R> flatMap(Func1<T, Flow<R>> flatFunc) {
        return new FlowPipeline<T, R>(this) {
            @Override
            public Sink<T> onWrapSink(Sink<R> sink) {
                return new SinkImpl<T, R>(sink) {

                    @Override
                    public void accept(T each) {
                        Flow<R> flat = flatFunc.apply(each);
                        flat.forEach(e -> downstream.accept(e));
                    }
                };
            }
        };
    }

    @Override
    public T reduce(Func2<T, T, T> reduceFunc) {
        Sink<T> reduceSink = new ReduceOp<>(reduceFunc);

        // 开始计算
        eval(this, reduceSink);

        T result = ((ReduceOp<T>)reduceSink).total;
        System.out.println("--> reduce result: " + result);
        return null;
    }

    @Override
    public <K, V> List<Pair2<K, V>> reduceByKey(Func1<Pair2, K> keyExtractor, Func1<Pair2, V> valueExtractor, Func2<V, V, V> reduceFunc) {
        ReduceByKeyOp op = new ReduceByKeyOp(keyExtractor, valueExtractor, reduceFunc);

        eval(this, op);

        return op.output();
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
        return map(t -> 1).reduce((a, b) -> a + b);
    }

    @Override
    public void forEach(Func0<T> forFunc) {
        eval(this, forFunc::apply);
    }

    void eval(FlowPipeline<B, T> lastPipe, Sink<T> terminalOp) {
        FlowPipeline cursor = lastPipe;
        Sink<T> finalSink = terminalOp;

        while (cursor.prevStage != null) {
            finalSink = cursor.onWrapSink(finalSink);
            cursor = cursor.prevStage;
        }

        List<T> originData = cursor.data;
        for(T t : originData) {
            finalSink.accept(t);
        }
    }

    public static class Head<B, T> extends FlowPipeline<B, T> {
        public Head(T... ts) {
            super(Arrays.asList(ts));

        }

        public Head(List<T> list) {
            super(list);

        }

        @Override
        public Sink<B> onWrapSink(Sink<T> sink) {
            return null;
        }
    }
}
