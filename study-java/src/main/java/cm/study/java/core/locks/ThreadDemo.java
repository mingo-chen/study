package cm.study.java.core.locks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadDemo {

    private static Logger ILOG = LoggerFactory.getLogger(ThreadDemo.class);

    public void interrupt() {
        Thread t1 = new Thread(() -> {
            boolean isRun = true;
            for (; isRun; ) {
                try {

                } catch (Exception ie) {
                    ILOG.error("search exception:", ie);
                    isRun = false;
                }
            }

            ILOG.info("thread run complete...");
        });

        t1.start();
        t1.interrupt();
        ILOG.info("has interrupt...");
    }

    public static void main(String[] args) {
        ThreadDemo demo = new ThreadDemo();
        demo.interrupt();

    }
}
