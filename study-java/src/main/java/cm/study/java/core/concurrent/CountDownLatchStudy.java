package cm.study.java.core.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchStudy {

    private static Logger ILOG = LoggerFactory.getLogger(CountDownLatchStudy.class);

    private int maxConcurrent = 2;

    public void doSomething() {
        CountDownLatch latch = new CountDownLatch(maxConcurrent);

        try {
            Thread t1 = new Thread(() -> {
                ILOG.info("complete A something...");

                latch.countDown();
                ILOG.info("after A count do: {}", latch.getCount());

            });

            Thread t2 = new Thread(() -> {
                ILOG.info("complete B something...");

                latch.countDown();
                ILOG.info("after B count do: {}", latch.getCount());

            });

            t1.start();
            t2.start();

            latch.await();
            ILOG.info("count do: {}", latch.getCount());

        } catch (Exception e) {
            ILOG.error("xxxx", e);
        }
    }

    public static void main(String[] args) {
        CountDownLatchStudy study = new CountDownLatchStudy();

        for (int i = 0; i < 10; i++) {
            study.doSomething();
        }
    }
}
