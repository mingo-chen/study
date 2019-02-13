package cm.study.java.core.concurrent;

import cm.study.java.core.utils.T;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 电梯调度系统
 */
public class ElevatorDispatch {

    private static Logger ILOG = LoggerFactory.getLogger(ElevatorDispatch.class);

    /**
     * 使用电梯事件
     */
    public static class Person {
        String ID;

        // 等待楼层
        int from;

        // 到达楼层
        int to;

        // 到达时间
        long arrivalTime;

        // 进入时间
        long enterTime;

        // 出去时间
        long exitTime;

        public Person(String person, int from, int to) {
            this.ID = person;
            this.from = from;
            this.to = to;
            this.arrivalTime = System.currentTimeMillis();
        }

        public void enter() {
            this.enterTime = System.currentTimeMillis();
            ILOG.info("person[{}进入了电梯, from: {}, to: {}, 等待时间: {} ms",
                    this.ID, this.from, this.to, this.enterTime - this.arrivalTime);
        }

        public void exit() {
            this.exitTime = System.currentTimeMillis();
            ILOG.info("person[{}]离开了电梯, from: {}, to: {}, 乘坐时间: {} ms",
                    this.ID, this.from, this.to, this.exitTime - this.enterTime);
        }

        @Override
        public String toString() {
            return "Person{" +
                   "ID='" + ID + '\'' +
                   ", from=" + from +
                   ", to=" + to +
                   ", arrivalTime=" + arrivalTime +
                   ", enterTime=" + enterTime +
                   ", exitTime=" + exitTime +
                   '}';
        }
    }

    /**
     * 电梯
     */
    public static class Elevator implements Runnable {
        String ID;

        // 当前的位置, 对应楼层信息
        int position;

        // 1 -> 向上
        // 0 -> 停止
        // -1 -> 向下
        int direction;

        // 楼层层数
        int floor;

        // 速度, 使用每一层运行时间来代替
        int speed;

        // 正在电梯里面的人
        List<Person> moving;

        ElevatorDispatch system;

        public Elevator(String name, int floor, int speed, ElevatorDispatch dispatch) {
            this.ID = name;
            this.floor = floor;
            this.speed = speed;
            this.system = dispatch;
            this.position = 1; // 1 ~ floor
            this.direction = 0;
            this.moving = new ArrayList<>();
        }

        /**
         * 有人从电梯出去
         */
        public void exit() {
            // 如果当前楼层有人要下, 开门, 把乘客放出去
            for (Iterator<Person> it = moving.iterator(); it.hasNext(); ) {
                Person person = it.next();
                if (person.to == this.position) {
                    it.remove();
                    person.exit();
                    ILOG.info("person[{}]离开电梯[{}]", person.ID, this.ID);
                }
            }
        }

        /**
         * 有人进入电梯
         */
        public void enter() {
            // 查看wait里有没有当前楼层的要上
            // 如果当前楼层有人要上, 开门, 把等待的人放进来
            synchronized (this.system.waits) {
                for (Iterator<Person> it = this.system.waits.iterator(); it.hasNext(); ) {
                    Person person = it.next();
                    if (person.from == this.position) {
                        it.remove();
                        person.enter();
                        this.moving.add(person);
                        ILOG.info("person[{}]进入电梯[{}]", person.ID, this.ID);
                    }
                }
            }
        }

        @Override
        public void run() {
            // 电梯运行中
            for (; ; ) {
                exit();

                enter();

                if (system.waits.size() == 0 && moving.size() == 0) { // 外面没有人在等待, 里面也没有人要送, 就停止运行
                    synchronized (this) {
                        try {
                            this.direction = 0;
                            ILOG.info("电梯停止, {}, {}", this.ID, this.position);
                            this.wait();
                        } catch (Exception e) {
                            ILOG.error("elevator wait exception", e);
                        }
                    }
                }

                // move, sleep speed seconds
                T.sleep(TimeUnit.SECONDS, speed);

                if(position == 1 && direction < 0) { // 在一楼向下运行, 需要改变运行方向
                    direction = -direction;

                } else if(position == floor && direction > 0) { // 在顶楼向上运行, 需要改变运行方向
                    direction = -direction;
                }

                position = position + direction;
                ILOG.info("电梯运行, ID: {}, direct: {}, position: {}", this.ID, this.direction, this.position);
            }
        }

        @Override
        public String toString() {
            return "Elevator{" +
                   "ID='" + ID + '\'' +
                   ", position=" + position +
                   ", direction=" + direction +
                   ", floor=" + floor +
                   ", speed=" + speed +
                   ", moving=" + moving +
                   ", system=" + system +
                   '}';
        }
    }

    /**
     * 几部电梯
     */
    private Elevator[] elevators;

    /**
     * 在外等待的列表
     */
    private volatile List<Person> waits;

    public ElevatorDispatch(int size, int floor, int speed) {
        waits = new ArrayList<>();
        elevators = new Elevator[size];
        for (int s = 0; s < size; s++) {
            elevators[s] = new Elevator("E-" + s, floor, speed, this);
            Thread elevatorThread = new Thread(elevators[s]);
            elevatorThread.start();
        }

    }

    /**
     * 使用电梯
     * 并发调用
     */
    public void use(Person person) {
        if(person.from != person.to) {
            waits.add(person);
            ILOG.info("来了个用户[{}]使用电梯, 所在楼层: {}, 要去楼层: {}", person.ID, person.from, person.to);

            // 通知一台电梯来接人
            // 是否需要唤醒一台电梯?
            boolean isAllRunning = true;
            for (Elevator elevator : elevators) {
                if (elevator.direction == 0) {
                    isAllRunning = false;
                }
            }

            // 都在运行, 没什么好调度的, 等着就行
            if(isAllRunning) {
                ILOG.debug("所有电梯都在运行, 等着...");
                return;
            }

            // 有电梯在睡觉, 如果正在运行的电梯方向相同且顺路, 就无需
            boolean isOnTheWay = false;
            for (Elevator elevator : elevators) {
                if(person.to > person.from) { // 向上
                    if(elevator.direction > 0 && elevator.position <= person.from) { // 同向且还没经过from
                        isOnTheWay = true;
                    }

                } else { // 向下
                    if(elevator.direction < 0 && elevator.position >= person.from) {
                        isOnTheWay = true;
                    }
                }
            }

            // 有顺路电梯, 稍上就好, 等着吧
            if(isOnTheWay) {
                ILOG.debug("有电梯正在过来, 等着...");
                return;
            }

            // 没有顺路的, 就找个最近的睡觉的电梯过来接人
            Elevator nearestElevator = null;
            int nearest = Integer.MAX_VALUE;

            for (Elevator elevator : elevators) {
                // 方向是否相同
                int distance = elevator.position - person.from;
                if (elevator.direction == 0 && distance < nearest) {
                    nearest = distance;
                    nearestElevator = elevator;
                }
            }

            if (nearestElevator != null) {
                synchronized (nearestElevator) {
                    if(nearestElevator.direction == 0) { // double check
                        nearestElevator.direction = person.from > nearestElevator.position ? 1 : -1;
                        nearestElevator.notify();
                        ILOG.info("notify Elevator[{}], position: {}, direct: {}, person: {}, from: {}, to: {}",
                                nearestElevator.ID, nearestElevator.position, nearestElevator.direction, person.ID, person.from, person.to);
                    } else {
                        ILOG.debug("最近的电梯已经被其它线程唤醒, 等着中...");
                    }
                }
            } else {
                ILOG.debug("没有最近电梯, wtf...");
            }

        } else {
            ILOG.warn("傻叼按错了, 按了当前的楼层, {}", person);
        }
    }

    public static void main(String[] args) {
        int floor = 10;
        ElevatorDispatch elevatorDispatch = new ElevatorDispatch(4, floor, 1);
        AtomicInteger count = new AtomicInteger(0);

        for (int n = 0; n < 8; n++) {
            int from = RandomUtils.nextInt(0, floor) + 1;
            int to = RandomUtils.nextInt(0, floor) + 1;

            Person person = new Person("C-" + count.addAndGet(1), from, to);
            elevatorDispatch.use(person);

            int interval = RandomUtils.nextInt(1, 5);
            T.sleep(TimeUnit.SECONDS, interval);

            if (n == 4) {
                T.sleep(TimeUnit.MINUTES, 1);
            }

        }
    }
}
