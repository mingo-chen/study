package cm.study.java.collection;

import cm.study.java.core.utils.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ArrayBlockingQueueStudy {

    private static Logger ILOG = LoggerFactory.getLogger(ArrayBlockingQueueStudy.class);

    public void add() {
        Queue<String> queue = new ArrayBlockingQueue<>(1);

        queue.add("cm");
        queue.add("ljx");
    }

    public void offer() throws Exception {
        ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<>(1);

        queue.offer("cm");
        queue.offer("ljx");

        boolean rt = queue.offer("test", 1, TimeUnit.SECONDS);
        System.out.println("--> "  + rt);
    }

    public void put() throws Exception {
        ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<>(1);

        Thread t1 = new Thread(()->{
            try {
                queue.put("cm");
                queue.put("ljx"); //会阻塞一直等待队列为空
                ILOG.info("put complete!");

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        t1.start();
        T.sleep(TimeUnit.SECONDS, 1);

        queue.poll();  // 没有这句使队列有空间, t1线程会一直阻塞状态
    }

    public void take() throws Exception {
        ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<>(1);

        new Thread(() -> {
            try {
                String value = queue.take();
                ILOG.info("get element from queue: {}", value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        queue.offer("cm");
    }

    public void poll() {
        ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<>(1);
        queue.offer("cm");

        ILOG.info("get value from queue: {}", queue.poll());
        ILOG.info("get value from queue: {}", queue.poll());
    }

    public void remove() {
        ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<>(1);
        queue.offer("cm");

        ILOG.info("get value from queue: {}", queue.remove());
        ILOG.info("get value from queue: {}", queue.remove());
    }

    public static void main(String[] args) throws Exception {
        ArrayBlockingQueueStudy study = new ArrayBlockingQueueStudy();
//        study.offer();
//        study.poll();

//        study.add();
        study.remove();

//        study.put();
//        study.take();
    }
}
