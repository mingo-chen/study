package cm.study.java.core.locks;

import cm.study.java.core.utils.Md5Util;
import cm.study.java.core.utils.T;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class ObjectNotifyTest {

    private static Logger ILOG = LoggerFactory.getLogger(ObjectNotifyTest.class);

    public Object consumerLock = new Object();

    public Object productLock = new Object();

    private BlockingQueue<String> queue = new ArrayBlockingQueue<>(100);

    public void p_do1(String message) {
        synchronized (consumerLock) {
            try {
                queue.add(message);
                consumerLock.notify();
                ILOG.info("receive message: {}, length: {}", message, queue.size());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void c_do2() {
        while (true) {
            synchronized (consumerLock) {
                try {
                    if (queue.isEmpty()) {
                        consumerLock.wait();
                        ILOG.debug("consumer message thread wait....");

                    } else {
                        String message = queue.poll();
                        ILOG.info("consumer message: {}, length: {}", message, queue.size());

                        T.sleep(TimeUnit.SECONDS, 1);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public static void main(String[] args) {
        ObjectNotifyTest notifyTest = new ObjectNotifyTest();

        Thread t1 = new Thread(() -> {
            while(true) {
                int times = RandomUtils.nextInt(5, 10);
                for(int n = 0; n < times; n++) {
                    String message = Md5Util.digest(String.valueOf(System.currentTimeMillis() + RandomUtils.nextInt()));
                    notifyTest.p_do1(message);
                }

                int sleepTime = RandomUtils.nextInt(400, 600);
                T.sleep(TimeUnit.MILLISECONDS, sleepTime);
                ILOG.info("product {} message complete", times);
            }
        });

        Thread t2 = new Thread(() -> notifyTest.c_do2());
        t2.start();
        t1.start();
    }
}
