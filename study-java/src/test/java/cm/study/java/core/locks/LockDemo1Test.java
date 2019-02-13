package cm.study.java.core.locks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LockDemo1Test {

    private static Logger ILOG = LoggerFactory.getLogger(LockDemo1Test.class);

    public static class Writer implements Runnable {

        private LockDemo1 demo1;

        public Writer(LockDemo1 demo1) {
            this.demo1 = demo1;
        }

        @Override
        public void run() {
            demo1.write();
        }
    }

    public static class Reader implements Runnable {

        private LockDemo1 demo1;

        private String name;

        public Reader(LockDemo1 demo1, String name) {
            this.demo1 = demo1;
            this.name = name;
        }

        @Override
        public void run() {
            try {
                ILOG.info("reader: {}", name);
                demo1.read(name);
            } catch (InterruptedException ie) {
                ILOG.info("不读了, 不等了");
            }

            ILOG.info("读结束");
        }
    }

    public static void main(String[] args) {
        LockDemo1 demo1 = new LockDemo1();
        Thread writeThread = new Thread(new Writer(demo1), "writer");
        Thread readThread1 = new Thread(new Reader(demo1, "aaa"), "reader-a");
        Thread readThread2 = new Thread(new Reader(demo1, "bbb"), "reader-b");
        Thread readThread3 = new Thread(new Reader(demo1, "ccc"), "reader-c");

        writeThread.start();
        readThread1.start();
        readThread2.start();
        readThread3.start();

//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                long start = System.currentTimeMillis();
//                for (; ; ) {
//                    if (System.currentTimeMillis() - start > 5000) {
//                        System.out.println("不等了, 尝试中断...");
//                        readThread1.interrupt();
//                        break;
//                    }
//                }
//            }
//        }).start();

    }
}