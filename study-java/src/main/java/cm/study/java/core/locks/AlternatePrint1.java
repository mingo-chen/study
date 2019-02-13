package cm.study.java.core.locks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AlternatePrint1 {
    int i = 0;
    List<Integer> output = new ArrayList<>();

    private static Logger ILOG = LoggerFactory.getLogger(AlternatePrint1.class);

    public static void main(String[] args) throws Exception {
        int SIZE = 1000;
        // 创建该类的对象
        for(int n = 0; n < 1; n++) {
            AlternatePrint1 obj = new AlternatePrint1();
            test(obj, SIZE);
            for (int i = 0; i < SIZE; i++) {
                if (i + 1 != obj.output.get(i)) {
                    System.out.println("--> " + obj.output);
                    break;
                }
            }
        }
    }

    static void test(AlternatePrint1 obj, int size) throws Exception {
        // 使用匿名内部类的形式，没创建runnable对象
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while(obj.i < size){
                    // 上锁当前对象
                    synchronized(this) {
                        // 唤醒另一个线程
                        notify();
                        ++obj.i;
//                        System.out.println("Thread " + Thread.currentThread().getName()  + " "+ ++obj.i);
                        ILOG.info("value: {}", obj.i);
                        obj.output.add(obj.i);

//                        try {
//                            Thread.currentThread();
//                            // 使其休眠100毫秒，放大线程差异
//                            Thread.sleep(100);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
                        try {
                            // 释放掉锁
                            wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                synchronized (this) {
                    this.notifyAll();
                }
            }
        };

        // 启动多个线程（想创建几个就创建几个）
        Thread t1 = new Thread(runnable);
        t1.start();

        Thread t2 = new Thread(runnable);
        t2.start();

        Thread t3 = new Thread(runnable);
        t3.start();

//        t1.join();
//        t2.join();
//        t3.join();
        TimeUnit.SECONDS.sleep(3);

    }
}
