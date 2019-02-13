package cm.study.java.core.concurrent;

import cm.study.java.core.utils.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class DataVisibility {

    private int value;

    private static Logger ILOG = LoggerFactory.getLogger(DataVisibility.class);

    public static void main(String[] args) {
        DataVisibility dataVisibility = new DataVisibility();
        dataVisibility.value = 1;

        Thread t1 = new Thread(() -> {
            ILOG.info("read value: {}", dataVisibility.value);

            T.sleep(TimeUnit.SECONDS, 2);

            synchronized (dataVisibility) {
                dataVisibility.value = dataVisibility.value + 1;
            }

            ILOG.info("after value: {}", dataVisibility.value);
        });

        t1.start();

        Thread t2 = new Thread(() -> {
            synchronized (dataVisibility) {
                dataVisibility.value = dataVisibility.value + 2;
            }

            ILOG.info("after value: {}", dataVisibility.value);
        });

        t2.start();
    }
}
