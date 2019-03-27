package cm.study.java.lang.reactive;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Java8 {

    public static void main(String[] args) {
        long count = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9)
                .stream()
//                .filter(e -> e % 2 == 1)
                .map(e -> e + 1)
//                .count();
                .reduce(0, (p1, p2) -> p1 + p2);
//                .collect(Collectors.toList());


        System.out.println("--> " + count);
    }
}
