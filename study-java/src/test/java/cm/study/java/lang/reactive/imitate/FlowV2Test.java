package cm.study.java.lang.reactive.imitate;

import cm.study.java.core.utils.U;
import org.testng.annotations.Test;

import java.util.List;

public class FlowV2Test {

    @Test
    public void test() {
        Flow<Integer> flow = StreamsV2.of(2, 2, 1, 4, 6, 8, 3, 9, 6);
        Flow<Integer> flow0 = flow.filter(e -> e % 2 == 1);
        Flow<Integer> flow1 = flow0.map(e -> e + 1);
        Integer result = flow1.reduce((e1, e2) -> e1 + e2);
        System.out.println("--> " + result);
    }

    @Test
    public void test_flatMap() {
        StreamsV2.of("I'm aaa", "I'm bbb", "I'm ccc").flatMap(line -> {
            String[] words = line.split(" ");
            return StreamsV2.of(words);
        }).forEach(w -> System.out.println(w));

    }

    @Test
    public void test_forEach() {
        StreamsV2.of(1, 2, 3).forEach(e -> System.out.println(e));
    }

    @Test
    public void test_reduce_by_key() {
        List<Pair2<String, Integer>> reduces = StreamsV2.of("A", "B", "C", "D", "A", "B", "C", "A", "B", "A")
                .map(word -> new Pair2(word, 1))
                .reduceByKey(pair2 -> (String) pair2.getKey(),
                        pair2 -> (Integer) pair2.getValue(),
                        (v1, v2) -> v1 + v2);

        for (Pair2<String, Integer> pair2 : reduces) {
            System.out.println("--> " + pair2);
        }
    }
}
