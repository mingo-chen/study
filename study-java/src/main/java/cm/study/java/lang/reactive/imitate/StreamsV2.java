package cm.study.java.lang.reactive.imitate;

import java.util.List;

/**
 * 流式API
 *
 * 从各种方式构建成Streamable, of, from, just...
 *
 * 对Streamable可以进行各种转换, map, reduce, filter, zip...
 *
 * 对于最终的结果进行最终输出, forEach, collection
 *
 * Streams.of(1, 2, 3).filter(e -> e%2 == 1).map(e -> e+1).forEach(print);
 */
public class StreamsV2 {

    /**
     * 把 x个 t变成流
     * @param ts
     * @param <T>
     * @return
     */
    public static <T> Flow<T> of(T... ts) {
//        Sink<T> sink = SinkImpl.of(ts);
        return new FlowPipeline.Head<>(ts);
    }

    public static <T> Flow<T> from(List<T> list) {
//        Sink<T> sink = SinkImpl.with(list);
        return new FlowPipeline.Head<>(list);
    }

    public static void main(String[] args) {

    }
}
