package cm.study.java.core.locks;

import org.testng.annotations.Test;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.testng.Assert.*;

public class BoundedQueueTest {
    private static final Random random = new Random(System.currentTimeMillis());

    @Test
    public void testAdd() {
    }

    @Test
    public void testRemove() {
    }

    static class Producter implements Runnable {
        private BoundedQueue queue;

        public Producter(BoundedQueue queue) {
            this.queue = queue;
        }

        public void produce() throws InterruptedException {
            queue.add(new Integer(random.nextInt(100)));
        }

        @Override
        public void run() {
            try {
                produce();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class Consumer implements Runnable {
        private BoundedQueue queue;

        public Consumer(BoundedQueue queue) {
            this.queue = queue;
        }

        public Integer remove() throws InterruptedException {
            return queue.remove();
        }

        @Override
        public void run() {
            try {
                remove();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        BoundedQueue queue = new BoundedQueue(5);

//        for (int i = 1; i <= 2000; i++) {
//            Thread thread = new Thread(new Producter(queue), String.valueOf(i));
//            thread.start();
//        }
//
//        for (int i = 1; i <= 2000; i++) {
//            Thread thread = new Thread(new Consumer(queue), String.valueOf(i));
//            thread.start();
//        }

        AtomicInteger addCount = new AtomicInteger(0);
        AtomicInteger delCount = new AtomicInteger(0);

        for (int n = 0; n < 100; n++) {
            final int idx = n;
            Thread t1 = new Thread(() -> {
                for(int x = 0; x < 100; x++) {
                    try {
                        if (random.nextBoolean()) {
                            queue.add(random.nextInt(100));
                            addCount.incrementAndGet();
                        } else {
                            queue.remove();
                            delCount.incrementAndGet();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                System.out.printf("--> thread[%d] complete \n", idx);
            });

            t1.start();
//            System.out.println("####");
        }

        System.out.println("---");
        TimeUnit.MINUTES.sleep(1);
        System.out.println("=> " + queue);
        System.out.println("=> " + addCount);
        System.out.println("=> " + delCount);
    }

}