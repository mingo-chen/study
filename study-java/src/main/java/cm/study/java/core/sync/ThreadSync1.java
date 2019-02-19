package cm.study.java.core.sync;

import cm.study.java.core.utils.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A, B线程执行完之后再执行C线程
 */
public class ThreadSync1 {

    private static Logger ILOG = LoggerFactory.getLogger(ThreadSync1.class);

    private Boolean aStatus = new Boolean(false);

    private Boolean bStatus = new Boolean(false);

    public void case1() {
        Thread a = new Thread(() -> {
            ILOG.info("do some thing a start...");
            T.sleep(TimeUnit.SECONDS, 1);
            ILOG.info("do some thing a complete!");

            synchronized (aStatus) {
                aStatus.notify();
                aStatus = true;
            }
        });

        Thread b = new Thread(() -> {
            ILOG.info("do some thing b start...");
            T.sleep(TimeUnit.SECONDS, 1);
            ILOG.info("do some thing b complete!");

            synchronized (bStatus) {
                bStatus.notify();
                bStatus = true;
            }
        });

        Thread c = new Thread(() -> {
            try {
                ILOG.info("do some thing c start...");
                ILOG.info("wait for a start...");
                while (aStatus == false) {
                    synchronized (aStatus) {
                        aStatus.wait();
                    }
                }

                ILOG.info("wait for a complete!");

                ILOG.info("wait for b start...");
                while (bStatus == false) {
                    synchronized (bStatus) {
                        bStatus.wait();
                    }
                }
                ILOG.info("wait for b complete!");

                ILOG.info("do some thing c complete!");
            } catch (Exception e) {

            }
        });

        c.start();
        b.start();
        T.sleep(TimeUnit.SECONDS, 3);
        a.start();
    }

    private CountDownLatch latch = new CountDownLatch(2);

    public void case2() {
        Thread a = new Thread(() -> {
            ILOG.info("do some thing a start...");
            T.sleep(TimeUnit.SECONDS, 1);
            ILOG.info("do some thing a complete!");

            latch.countDown();
        });

        Thread b = new Thread(() -> {
            ILOG.info("do some thing b start...");
            T.sleep(TimeUnit.SECONDS, 1);
            ILOG.info("do some thing b complete!");

            latch.countDown();
        });

        Thread c = new Thread(() -> {
            ILOG.info("do some thing c start...");

            try {
                latch.await();
            } catch (Exception e) {
                e.printStackTrace();
            }

            ILOG.info("do some thing c complete!");
        });

        c.start();
        T.sleep(TimeUnit.SECONDS, 1);
        a.start();
        b.start();
    }

    private ArrayBlockingQueue<Boolean> syncQueue = new ArrayBlockingQueue<>(2);

    public void case3() {
        Thread c = new Thread(() -> {
            ILOG.info("do some thing c start...");
            try {
                boolean a = syncQueue.take();
                boolean b = syncQueue.take();

                ILOG.info("do some thing c complete!");

            } catch (Exception e) {
                e.printStackTrace();
            }

        });

        Thread a = new Thread(() -> {
            ILOG.info("do some thing a start...");
            T.sleep(TimeUnit.SECONDS, 1);
            ILOG.info("do some thing a complete!");

            syncQueue.add(true);
        });

        Thread b = new Thread(() -> {
            ILOG.info("do some thing b start...");
            T.sleep(TimeUnit.SECONDS, 1);
            ILOG.info("do some thing b complete!");

            syncQueue.add(true);
        });

        c.start();
        T.sleep(TimeUnit.SECONDS, 1);
        a.start();
        b.start();
    }

    private Object lock = new Object();
    private AtomicInteger wait = new AtomicInteger(0);

    public void case4() {
        Thread c = new Thread(() -> {
            ILOG.info("do some thing c start...");

            synchronized (lock) {
                try {
                    lock.wait();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            ILOG.info("do some thing c complete!");

        });

        Thread a = new Thread(() -> {
            ILOG.info("do some thing a start...");
            T.sleep(TimeUnit.SECONDS, 1);
            ILOG.info("do some thing a complete!");

            synchronized (lock) {
                if(wait.incrementAndGet() >= 2) {
                    lock.notify();
                }
            }

        });

        Thread b = new Thread(() -> {
            ILOG.info("do some thing b start...");
            T.sleep(TimeUnit.SECONDS, 1);
            ILOG.info("do some thing b complete!");

            synchronized (lock) {
                if(wait.incrementAndGet() >= 2) {
                    lock.notify();
                }
            }
        });

        b.start();
        T.sleep(TimeUnit.SECONDS, 2);
        c.start();
        T.sleep(TimeUnit.SECONDS, 2);
        a.start();
    }

    public static void main(String[] args) {
        ThreadSync1 sync1 = new ThreadSync1();
//        sync1.case1();
//        sync1.case2();
//        sync1.case3();
        sync1.case4();
    }
}
