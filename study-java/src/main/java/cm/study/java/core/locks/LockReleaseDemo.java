package cm.study.java.core.locks;

import cm.study.java.core.utils.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class LockReleaseDemo {

    private static Logger ILOG = LoggerFactory.getLogger(LockReleaseDemo.class);
    private ReentrantLock lock = new ReentrantLock();

    public void aThing(int cost) {
        lock.lock();

        try {
            ILOG.info("start to do a thing...");
            T.sleep(TimeUnit.SECONDS, cost);
            ILOG.info("complete to do a thing!");

        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws Exception {
        LockReleaseDemo demo = new LockReleaseDemo();

        Thread t1 = new Thread(() -> demo.aThing(10));
        t1.start();

        Thread t2 = new Thread(() -> demo.aThing(1));
        t2.start();

        t1.join();

        ILOG.info("complete release demo...");
    }
}
