package cm.study.java.core.locks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.management.Agent;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class AqsDemo extends AbstractQueuedSynchronizer {

    private static final long serialVersionUID = 7246416562059354360L;

    private static Logger ILOG = LoggerFactory.getLogger(AqsDemo.class);

    private int count = 0;

    @Override
    protected boolean tryAcquire(int arg) {
//        boolean acq = count++ % 2 == 0;
//        ILOG.info("search acq: {}", acq);
//        return acq;
        return false;
    }

    public static void main(String[] args) throws Exception {
        AqsDemo aqs = new AqsDemo();

        Thread t1 = new Thread(() -> {
            aqs.acquire(1);
            ILOG.info("thread unpark and run here...");
        });

        t1.start();

//        Thread t2 = new Thread(() -> {
//            aqs.acquire(1);
//            ILOG.info("thread unpark and run here...");
//        });
//
//        t2.start();

        t1.join();
//        t2.join();

//        aqs.release(1);

        ILOG.info("AQS: {}", aqs);
    }
}
