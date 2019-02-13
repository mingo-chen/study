package cm.study.java.core.concurrent;

import java.util.concurrent.TimeUnit;

public class Interrupt {

    public static void main(String[] args) throws Exception {
        Thread t1 = new Thread() {

            @Override
            public void run() {
                try {
                    int a = 1;
                    while (true) {
                        TimeUnit.SECONDS.sleep(1);
//                        a = a + 1;
                    }

                } catch (Exception e) {
                    System.out.println("Interruted When Sleep");
                    boolean interrupt = this.isInterrupted();
                    //中断状态被复位
                    System.out.println("interrupt:"+interrupt);
                }
            }
        };


        t1.start();
        TimeUnit.SECONDS.sleep(3);
        t1.interrupt();
    }
}
