package cm.study.java.simulation;

import org.apache.commons.lang3.RandomUtils;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class People extends Thread {
//        private int money;
    private AtomicInteger money;
    private Context context;

    private static int times = 0;

    private static final int MAX_TIMES = 300;

    public People(int money, int index, Context context) {
        setName("people-" + index);
        this.money = new AtomicInteger(money);
        this.context = context;
    }

    @Override
    public void run() {
        // 随机给某人发一块钱
        while (money.get() > 0 && times < MAX_TIMES) {
            People selectPeople = context.selectPeople();

            this.incr();
            selectPeople.decr();
            System.out.println("[" + getName() + "] -> [" + selectPeople.getName() + "], " + this.money + ", " + selectPeople.money);

            times++;

            RichAndPoorDemo.sleep(40);
        }

        System.out.println("["+getName()+"]---> " + money);
    }

    void incr() {
//            this.money++;
        money.incrementAndGet();
    }

    void decr() {
//            this.money--;
        money.decrementAndGet();
    }
}
