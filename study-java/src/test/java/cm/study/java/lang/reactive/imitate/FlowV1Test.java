package cm.study.java.lang.reactive.imitate;

import cm.study.java.core.utils.U;
import cm.study.java.jmh.HelloWorldBTest;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.testng.Assert.*;

public class FlowV1Test {

//    @Benchmark
//    @BenchmarkMode(Mode.Throughput)
//    @OutputTimeUnit(TimeUnit.SECONDS)
    @Test
    public void test() {
        Flow<Integer> flow = Streams.of(2, 2, 1, 4, 6, 8, 3, 9, 6);
        U.assertEquals(flow.filter(e -> e > 0).count(), 9, "Flow.count implement error");

//        Comparator<Integer> cmp = (o1, o2) -> Integer.compare(o1, o2);
        Comparator<Integer> cmp = Comparator.comparingInt(Integer::intValue);

        U.assertEquals(flow.min(cmp), 1, "Flow.min implement error");
        U.assertEquals(flow.max(cmp), 9, "Flow.max implement error");

        U.assertEquals(flow.sorted(cmp).collection(), Arrays.asList(1, 2, 2, 3, 4, 6, 6, 8, 9), "Flow.sorted implement error");
        U.assertEquals(flow.distinct().collection(), Arrays.asList(2, 1, 4, 6, 8, 3, 9), "Flow.distinct implement error");

        U.assertEquals(flow.reduce((Integer p1, Integer p2) -> p1 + p2), 41, "Flow.reduce implement error");

        Flow<String> lines = Streams.of("I am cm", "I am xxx", "I am easy", "I am go");
        List<String> words = lines.flatMap(line -> Streams.of(line.split(" "))).collection();

        U.assertEquals(words, Arrays.asList("I", "am", "cm", "I", "am", "xxx", "I", "am", "easy", "I", "am", "go"), "Flow.flatMap implement error");

        List<Pair2<String, Integer>> pairs = lines.flatMap(line -> {
            List<Pair2<String, Integer>> pair2s = Arrays.stream(line.split(" "))
                    .map(word -> new Pair2<>(word, 1))
                    .collect(Collectors.toList());
            return Streams.from(pair2s);

        }).reduceByKey(pair -> (String)pair.getKey(),
                pair -> (Integer) pair.getValue(),
                (v1, v2) -> v1 + v2);

        System.out.println(pairs);
    }

    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
                .include(FlowV1Test.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}