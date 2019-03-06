package cm.study.java.thread;

import cm.study.java.core.utils.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class ThreadStatusStudy {

    private static Logger ILOG = LoggerFactory.getLogger(ThreadStatusStudy.class);

    public static void main(String[] args) throws Exception {
        Object lock = new Object();

        Thread t1 = new Thread(() -> { // sleep, wait(time), join(time) => time-waiting
            synchronized (lock) {
                T.sleep(TimeUnit.DAYS, 1);
            }

            ILOG.info("t1 sleep one day after!");

        }, "t1-with-TIMED-WAITING");

        t1.start();
//        t1.interrupt();

        Thread t2 = new Thread(() -> { // monitor enter => blocked
            synchronized (lock) {
                ILOG.info("t2 get lock and end!");
            }
        }, "t2-with-BLOCKED");

        t2.start();
//        t2.interrupt(); // 中断不了, 只能中断处于waiting状态的线程

        Object lock2 = new Object();
        /*
         wait, park, join会使线程处于waiting状态
         notify/notifyAll 会唤醒处于wait的线程
         unpark会唤醒通过park挂起的线程
          */
        Thread t4 = new Thread(() -> {
            synchronized (lock2) {
                try {
                    lock2.wait();
                } catch (Exception e) {
                    ILOG.error("t4 error", e);
                }
            }
        }, "t4-with-waiting");
        t4.start();
//        t4.interrupt(); // 可以中断一直处于waiting状态的t4; 如果没有这句, t4会一直处于wait时

        Thread t3 = new Thread("t3-with-time-waiting"){

            @Override
            public void run() {
                while (!this.isInterrupted()) {
                }
            }
        };

        t3.start();
//        t3.interrupt();
//        t3.join(100000);  // t3永远没执行结束, main线程就会处于wait状态

        ILOG.info("complete!!!");
    }

}
