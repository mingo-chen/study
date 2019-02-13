package cm.study.java.core.locks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class LockDemo1 {

    private static Logger ILOG = LoggerFactory.getLogger(LockDemo1.class);

    private Object lock;

    private ReentrantLock reentrantLock = new ReentrantLock();

    public LockDemo1() {
        this.lock = this;
    }

    public void write() {
        reentrantLock.lock();
        try {
            long startTime = System.currentTimeMillis();
            ILOG.info("开始写数据");

            for (; ; ) {
                if (System.currentTimeMillis() - startTime > TimeUnit.MINUTES.toMillis(1)) {
                    break;
                }
            }

            ILOG.info("写数据完成");

        } finally {
            reentrantLock.unlock();
        }
    }

    public void read(String name) throws InterruptedException {
        ILOG.info("开始读数据...");
        reentrantLock.lock();
        try {
            ILOG.info("正在高并发环境中读取数据");
        } finally {
            reentrantLock.unlock();
        }

        ILOG.info("读数据完成");
    }
}
