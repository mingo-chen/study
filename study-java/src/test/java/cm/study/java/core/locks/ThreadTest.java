package cm.study.java.core.locks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

public class ThreadTest {

    private static Logger ILOG = LoggerFactory.getLogger(ThreadTest.class);

    @Test
    public void testJoin() throws Exception {
        ILOG.info("test start");

        Thread t1 = new TaskThread(4);
        Thread t2 = new TaskThread(3);
        Thread t3 = new TaskThread(2);

        t1.start();
        t2.start();
        t1.join();
        t3.start();

        t2.join();
        t3.join();

        ILOG.info("test end");
    }

    public static class TaskThread extends Thread {
        public TaskThread(int cost) {
            super(new Runnable() {
                @Override
                public void run() {
                    ILOG.info("task handle begin...");

                    try {
                        TimeUnit.SECONDS.sleep(cost);
                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                    ILOG.info("task handle complete");
                }
            });
        }
    }

    @Test
    public void testSuspend() throws Exception {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                test();
            }
        });
        t1.setName("test");
        t1.start();

        TimeUnit.SECONDS.sleep(2);

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                ILOG.info("尝试执行独占的模块");
                test();
            }
        });
        t2.setName("pppp");
        t2.start();

        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                ILOG.info("释放独占模块");
                t1.resume();
            }
        });
        t3.start();

        t2.join();

    }

    public synchronized void test() {
        if (Thread.currentThread().getName().equalsIgnoreCase("test")) {
            ILOG.info("线程要独占此模块, 线程被挂起");
            Thread.currentThread().suspend();
        }

        ILOG.info("线程进入独占模块");
    }
}
