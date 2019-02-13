package cm.study.java.core.concurrent;

import cm.study.java.core.utils.T;
import org.apache.commons.lang3.RandomUtils;
import org.testng.annotations.Test;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ReduceTest {

    @Test
    public void testIncr() {
        Queue<Integer> queue = new ArrayDeque<>();
        queue.add(100);
        queue.add(200);
        queue.add(300);

        int v1 = queue.poll();
        System.out.println("--> " + v1 + ", " + queue);
    }

    public static void main(String[] args) throws Exception {
        Reduce reduce1 = new ReduceByCSP();
        Reduce reduce2 = new ReduceByCSP();
        System.out.println("--> invoke by executor: " + invokeByExecutor(reduce1));
//        System.out.println("--> invoke by thread: " + invokeByThread(reduce2));
    }

    /**
     * 多线程环境下无法保证数据正确性
     */
    public static boolean invokeByExecutor(Reduce reduce) throws Exception {
        AtomicInteger total = new AtomicInteger(0);
        ExecutorService exec = Executors.newFixedThreadPool(32);
        List<Future<Boolean>> futures = new ArrayList<>();

        for (int x = 0; x < 16; x++) {
            final int idx = x;
            Future<Boolean> future = exec.submit(() -> {
                int times = RandomUtils.nextInt(100, 200);
                for(int i = 0; i < times; i++) {
                    reduce.increment();
                    total.incrementAndGet();
                }

//                System.out.printf("thread[%d] complete\n", idx);
                return true;
            });
            futures.add(future);

        }

        for (Future<Boolean> future : futures) {
            future.get();
        }

        exec.shutdown();
        TimeUnit.SECONDS.sleep(3);
        System.out.println("--> " + reduce + ":" + total);
        return reduce.getV() == total.get();
    }

    /**
     * 多线程环境下无法保证数据正确性
     */
    public static boolean invokeByThread(Reduce reduce) throws Exception {
        AtomicInteger total = new AtomicInteger(0);

        for (int x = 0; x < 32; x++) {
            final int idx = x;
            Thread t1 = new Thread(() -> {
                int times = RandomUtils.nextInt(100, 200);
                for(int i = 0; i < times; i++) {
                    reduce.increment();
                    total.incrementAndGet();
                }

                T.sleep(TimeUnit.MILLISECONDS, 500);
//                System.out.printf("thread[%03d] complete\n", idx);
            });

            t1.start();
//            t1.join();
        }

        TimeUnit.SECONDS.sleep(3);
        System.out.println("--> " + reduce + ":" + total);
        return reduce.getV() == total.get();
    }
}