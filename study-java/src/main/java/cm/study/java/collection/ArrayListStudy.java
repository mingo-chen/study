package cm.study.java.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayListStudy {

    public static void main(String[] args) {
//        listGrow();

        iterator();
    }

    public static void iterator() {
        List<Integer> numbers = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));

        /**
         * jvm内部实现隐式采用Iterator来遍历list
         * 通过
         */
        for (Integer num : numbers) {
            if(num % 2 == 0) {
                numbers.remove(num);
            }
        }

        System.out.println("--> " + numbers);
    }

    /**
     * 默认是0size
     * 首次Add会初始化为10个长度的数组
     * 每次扩容是+50%
     */
    public static void listGrow() {
        List<Integer> numbers = new ArrayList<>();
        numbers.add(1);
        numbers.add(2);
        numbers.add(3);
        numbers.add(4);
        numbers.add(5);
        numbers.add(6);
        numbers.add(7);
        numbers.add(8);
        numbers.add(9);
        numbers.add(10);
        numbers.add(11);

        System.out.println("---> " + numbers);
    }
}
