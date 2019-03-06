package cm.study.java.core.locks;

import cm.study.java.core.utils.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class AQSStudy {

    private static Logger ILOG = LoggerFactory.getLogger(AQSStudy.class);

    private ReentrantLock lock = new ReentrantLock();

    private int value;

    public int getValue() {
        try {
            lock.lock();                // 当获取lock处于等待状态时, 线程的Interrupt事件不能终止等待
//            lock.lockInterruptibly(); // 当获取lock处于等待状态时, 线程的Interrupt事件可终止等待

            try {
                ILOG.info("get method complete, value: {}", value);
                return value;
            } finally {
                lock.unlock();
            }

        } catch (Exception e) {
            ILOG.error("get value catch ex", e);
            throw new RuntimeException(e);
        }
    }

    public void setValue(int value) {
        try {
            lock.lockInterruptibly();

            try {
                this.value = value;
                ILOG.info("set method complete, value: {}", value);
            } finally {
//                lock.unlock(); // 不释放锁, 让读线程hand住
            }

        } catch (Exception e) {
            ILOG.error("set value catch ex", e);
        }
    }

    public static void main(String[] args) throws Exception {
        AQSStudy study = new AQSStudy();

        // 启动2个线程, thread1设置value, 但不释放锁, thread2读取value, 应该会在等待队列中
        // 在main线程中, 调用thread2的中断方法
        Thread t1 = new Thread(() -> study.setValue(5));
        Thread t2 = new Thread(() -> System.out.println("==> " + study.getValue()));

        t1.start();
        t2.start();

//        t1.join();
//        t2.join();

        T.sleep(TimeUnit.SECONDS, 5);
        t2.interrupt();
        ILOG.info("has interrupt...");
    }
}
