package cm.study.java.core.locks;

import cm.study.java.core.utils.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RWLockStudy {

    private static Logger ILOG = LoggerFactory.getLogger(RWLockStudy.class);

    private ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock(false);

    private int value;

    public int read() {
        ReentrantReadWriteLock.ReadLock readLock = rwLock.readLock();
        readLock.lock();

        try {
            ILOG.info("read value: {}", value);
            T.sleep(TimeUnit.SECONDS, 3);
            return value;

        } finally {
            readLock.unlock();
        }
    }

    public void write(int newValue) {
        ReentrantReadWriteLock.WriteLock writeLock = rwLock.writeLock();
        writeLock.lock();

        try {
            value = newValue;
            T.sleep(TimeUnit.SECONDS, 3);
            ILOG.info("set new value: {}", newValue);
        } finally {
            writeLock.unlock();
        }
    }

    public static void main(String[] args) throws Exception {
        RWLockStudy study = new RWLockStudy();

        Thread wThread1 = new Thread(() -> study.write(5));

        Thread wThread2 = new Thread(() -> study.write(3));
        wThread1.start();
        wThread2.start();

        wThread1.join();
        wThread2.join();

        Thread rThread1 = new Thread(() -> study.read());
        rThread1.start();


        Thread rThread2 = new Thread(() -> study.read());
        rThread2.start();

        Thread rThread3 = new Thread(() -> study.read());
        rThread3.start();

        T.sleep(TimeUnit.SECONDS, 5);
        ILOG.info("task call complete!");
    }
}
