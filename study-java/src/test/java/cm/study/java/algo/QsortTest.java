package cm.study.java.algo;

import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.util.*;

import static org.testng.Assert.*;

public class QsortTest {

    private static Logger ILOG = LoggerFactory.getLogger(QsortTest.class);

    @Test
    public void testSort() {
//        List<Integer> numbers = Arrays.asList(6, 7, 8, 5);
        List<Integer> numbers = Arrays.asList(3, 75, 44, 79, 34, 96, 1, 15, 67, 68);
        List<Integer> result = new Qsort().sort(numbers);
        System.out.println("==> " + result);
    }

    @Test
    public void testSort_N() {
        long total1 = 0;
        long total2 = 0;

        for(int n = 0; n < 10; n++) {
            List<Integer> numbers = mockData(100, 0, 10000);
            Qsort qsort = new Qsort();

            long t1 = System.currentTimeMillis();
            List<Integer> afterQsort = qsort.sort2(numbers);
            long t2 = System.currentTimeMillis();
            List<Integer> afterJdk = new ArrayList<>(numbers);
            Collections.sort(afterJdk);
            long t3 = System.currentTimeMillis();

            total1 += (t2 - t1);
            total2 += (t3 - t2);
//            ILOG.info("after qsork: {}", afterQsort);
//            ILOG.info("after jdk: {}", afterJdk);

            assertTrue(isEquals(afterQsort, afterJdk));
        }

        ILOG.info("qsork: {}", total1);
        ILOG.info("jdk: {}", total2);
    }

    List<Integer> mockData(int size, int start, int end) {
        List<Integer> numbers = new ArrayList<>();

        for(int n = 0; n < size; n++) {
            numbers.add(RandomUtils.nextInt(start, end));
        }

        return numbers;
    }

    boolean isEquals(List<Integer> list1, List<Integer> list2) {
        int size1 = list1.size();
        int size2 = list2.size();
        if (size1 != size2) {
            return false;
        }

        for (int x = 0; x < size1; x++) {
            if (list1.get(x) != list2.get(x)) {
                return false;
            }
        }

        return true;
    }
}