package cm.study.java.stream;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class StreamExample {

    public static void sdy_lambda() {
        List<String> names = Arrays.asList("ljx", "cm");

        // 最原始写法, 匿名内部类
        names.forEach(new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println(s);
            }
        });

        System.out.println("---------");

        // lambda写法
        names.forEach( s-> {
            System.out.println(s);
        });

        System.out.println("---------");

        // 最简写法, lambda + 方法引用
        names.forEach(System.out::println);
    }

    public static void sdy_stream() {
        Optional<String> element = Stream.of("ljx", "cm").findFirst();
        System.out.println(element.get());
    }

    public static void main(String[] args) {
//        sdy_lambda();

        sdy_stream();
    }

}
