package cm.study.java.algo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Qsort {

    private static Logger ILOG = LoggerFactory.getLogger(Qsort.class);

    public List<Integer> sort(List<Integer> numbers) {
        rank(numbers, 0, numbers.size() - 1);
        return numbers;
    }

    public static List<Integer> sort2(List<Integer> l) {
        if (l.size() == 0) return l;
        Integer pivot = l.get(0);
        List<Integer> lLower = new ArrayList<Integer>();
        List<Integer> lHigher = new ArrayList<Integer>();
        for (Integer e : l) {
            if (e < pivot) {
                lLower.add(e);
            }
            if (e > pivot) {
                lHigher.add(e);
            }
        }
        List<Integer> sorted = new ArrayList<Integer>();
        sorted.addAll(sort2(lLower));
        sorted.add(pivot);
        sorted.addAll(sort2(lHigher));
        return sorted;
    }

    void rank(List<Integer> numbers, int start, int end) {
        if(start < end) {
            int threshold = numbers.get(start);

            int i = start;
            int j = end;
            int x = start;
            boolean forward = true;

            for (; i <= j; ) {
                if (forward) { // 从后往前
                    // 找出所有小于threshold的进行交换
                    if (numbers.get(j) < threshold) {
                        numbers.set(x, numbers.get(j));
                        x = j;
                        forward = !forward;
                    }
                    j--;

                } else { // 从前往后
                    // 找出所有大于threshold的进行交换
                    if (numbers.get(i) > threshold) {
                        numbers.set(x, numbers.get(i));
                        x = i;
                        forward = !forward;
                    }
                    i++;

                }
            }

            // i == j
            numbers.set(x, threshold);
//            ILOG.debug("numbers: {}, start: {}, end: {}", numbers, start, end);

            // 对左边进行递归排序
            if(start < x - 1) {
//                ILOG.debug("numbers: {}, start: {}, i: {}, sub: {}", numbers, start, x - 1, numbers.subList(start, x));
                rank(numbers, start, x - 1);
            }

            // 对右边进行递归排序
            if(x + 1 < end) {
//                ILOG.debug("numbers: {}, i: {}, end: {}, sub: {}", numbers, x + 1, end, numbers.subList(x+1, end+1));
                rank(numbers, x + 1, end);
            }
        }
    }
}
