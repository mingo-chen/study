package cm.study.java.core.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.Queue;

public class ReduceByCSP implements Reduce {

    private static Logger ILOG = LoggerFactory.getLogger(ReduceByCSP.class);

    private int value;

    private Queue<Integer> message = new ArrayDeque<>();

    public ReduceByCSP() {
        Thread consumer = new Thread(() -> {
            long s = System.currentTimeMillis();

            for (; ; ) {
                synchronized (message) {
                    Integer offset = message.poll();
                    if (offset != null) {
                        value += offset;
                        ILOG.info("poll from message queue, {}, {}, {}", offset, value, message.size());

                        s = System.currentTimeMillis();
                    }
                }

                if (System.currentTimeMillis() - s > 5000) {
                    break;
                }
            }
        });

        consumer.start();
    }

    @Override
    public int increment() {
        synchronized (message) {
            message.add(1);
        }
        ILOG.info("add to message queue: {}", message.size());

        return message.size();
    }

    @Override
    public int getV() {
        return value;
    }

    @Override
    public String toString() {
        return "ReduceStudy{" +
               "value=" + value +
               '}';
    }
}
