package cm.study.rxjava.eds;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 电梯对象
 * 对于电梯而言, 人可分为两类
 * 一类是处于里面乘坐的人群
 * 一类是在外等待的人群
 *
 * 默认电梯是处于一楼
 * 当来一个人后, 调度系统询问每台电梯, 最快到达指定楼层的时间, 选择最快的一台电梯做为最佳电梯
 * 把该人放到指定队列
 *
 * Created by chenming on 2017/2/26.
 */
public class Elevator extends Thread {

    private static final Logger ILOG = LoggerFactory.getLogger( Elevator.class );

    public static final int MAX_CAPCITY = 1200;

    /** 电梯通过每层楼的时间, 单位秒 */
    private int speed;

    /** 总楼层 */
    private int totalFloor;

    /** 电梯调度系统 */
    private DispatchSystem context;

    /** 当前承重 */
    private volatile int capcity;

    /** 运行方向, true向上, false向下 */
    private volatile boolean direction;

    /** 当前楼层 */
    private volatile int floor;

    /**
     * 电梯状态
     * 0 -> 停止
     * 1 -> 运行中
     * 2 -> 开门
     * 3 -> 关门
     * -1 -> 故障
     */
    private volatile int status;

    /** 最新要求的到达楼层 */
    private volatile int lastestToFloor;

    /** 最新请求的楼层 */
    private volatile int lastestCallFloor;

    public List<People> passengers = new ArrayList<>();

    public Elevator(String name, int speed, int totalFloor) {
        super.setName(name);
        this.speed = speed;
        this.totalFloor = totalFloor;
        this.capcity = 0;
        this.floor = 1; // 默认都是处于1层
        this.direction = true; // 在1层, 当然是向上
    }

    @Override
    public void run() {
        for(;;) {
            if(status != 0) {
                boolean openDoor = isNeedOpenDoor();
                if(openDoor) {
                    ILOG.info("[{}]打开电梯门...", getName());

                    out();

                    in();
                    ILOG.info("[{}]关闭电梯门...", getName());
                }

                move();
            } else {
                EdsUtils.sleep(100);

            }

            if(status == 0) {
//                ILOG.debug("电梯处于静止状态, name:{}", name);
            } else {
//                ILOG.info("电梯运行状态, name:{}, status:{}, floor:{}, direction:{}, capcity:{}", getName(), status, floor, direction, capcity);
            }
        }
    }

    boolean isNeedOpenDoor() {
        for(People people : passengers) {
            if(people.getTargetFloor() == floor) {
                return true;
            }
        }

        if(capcity >= MAX_CAPCITY * 0.9) {
            return true;
        }

        List<People>  waitPeoples = direction? context.getUp_wait_peoples() : context.getDown_wait_peoples();
        for(People people : waitPeoples) {
            if(people.getStartFloor() == floor) {
                return true;
            }
        }

        return false;
    }

    void out() {
        // 每到一层, 检查下, 有无人下
        for (Iterator<People> it = passengers.iterator(); it.hasNext(); ) {
            People people = it.next();
            if (people.getTargetFloor() == floor) {
                people.outElevator();
                capcity -= people.getWeight();
                it.remove();
                ILOG.info("有人出电梯, 当前乘客数:{}, 重量:{}, floor:{}", passengers.size(), capcity, floor);
            }
        }

    }

    void in() {
        if (capcity >= MAX_CAPCITY*0.9) {
            // 如果没有人下, 且电梯承载已大最大, 直接Pass
            ILOG.warn("电梯已达饱和, 跳过此层! floor:{}, capcity:{}", floor, capcity);
            return;
        }

        List<People>  waitPeoples = direction? context.getUp_wait_peoples() : context.getDown_wait_peoples();
        // 如果还有空间, 则问调度系统有无等待人群
        for(Iterator<People> it = waitPeoples.iterator(); it.hasNext();) {
            People people = it.next();
            if(people.getStartFloor() == floor && people.getWeight() + capcity < MAX_CAPCITY) {
                try {
                    people.inElevator(this);
                    this.capcity += people.getWeight();
                    passengers.add(people);
                    ILOG.info("有人进电梯, 当前乘客数:{}, 重量:{}, floor:{}", passengers.size(), capcity, floor);
                } catch (Exception e) {
                    // 已经进入电梯, 并发
                    System.out.println("进入电梯异常, msg:" + e.getMessage());
                }

                it.remove();
            }
        }
    }

    /**
     * 计算电梯到指定楼层的距离
     * @param targetFloor
     * @return
     */
    public int distance(int targetFloor, boolean peopleDirection) {
        if (floor > targetFloor) { // 电梯当前楼层比人楼层高
            if(peopleDirection) { // 人想上
                if (direction) { // 电梯向上
                    return totalFloor - floor + totalFloor - targetFloor;
                } else { // 电梯向下,或静止
                    return floor - targetFloor;
                }

            } else {
                if(direction) {
                    return totalFloor - floor + totalFloor - targetFloor;
                } else {
                    return floor - targetFloor;
                }

            }

        } else if (floor < targetFloor) { // 电梯当前楼层比人楼层低
            if(peopleDirection) { // 人想上
                if (!direction) {
                    return floor + targetFloor;
                } else {
                    return targetFloor - floor;
                }

            } else { // 人想下
                if(!direction) { // 电梯向下
                    return floor + targetFloor;
                } else { // 电梯向上
                    return targetFloor - floor;
                }

            }

        } else { // 电梯当前楼层跟人楼层相同
            if(peopleDirection) {
                if(direction) {
                    return 0;
                } else {
                    return targetFloor + targetFloor;
                }

            } else {
                if(!direction) {
                    return 0;
                } else {
                    return totalFloor - floor + totalFloor - floor;
                }
            }

        }

    }

    /**
     * 请求电梯
     */
    public void call(boolean direction, int callFloor) {
        if(status == 0) { // 静态状态
            this.direction = direction;
            this.lastestCallFloor = callFloor;
            status = 1;
            ILOG.warn("[{}]被唤醒!!!", getName());
        } else {
            // 非静止状态, 就不能改变
        }
    }

    /**
     * 每次移动一层
     */
    public void move() {
        if(lastestToFloor == floor && passengers.isEmpty()) {
            status = 0;
            lastestToFloor = -1;
            ILOG.warn("[{}]电梯为空, 静止状态!!! floor:{}", getName(), floor);
            return;
        }

        if(direction) {
            if(floor < totalFloor) {
                EdsUtils.sleep(speed * 1000);
                floor++;
                ILOG.info("[{}]电梯向上移动..., floor:{}", getName(), floor);

            } else {// 电梯非空, 朝下运行
                direction = false;
                ILOG.warn("[{}]电梯非空, 改变方向, 向下运行!!!", getName());
            }

        } else {
            if(floor > 1) {
                EdsUtils.sleep(speed * 1000);
                floor--;
                ILOG.info("[{}]电梯向下移动..., floor:{}", getName(), floor);

            } else {// 电梯非空, 朝上运行
                direction = true;
                ILOG.warn("[{}]电梯非空, 改变方向, 向上运行!!!", getName());

            }

        }

        if(lastestCallFloor == floor) {
            lastestCallFloor = -1;
        }

    }

    public void setContext(DispatchSystem context) {
        this.context = context;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getTotalFloor() {
        return totalFloor;
    }

    public void setTotalFloor(int totalFloor) {
        this.totalFloor = totalFloor;
    }

    public int getCapcity() {
        return capcity;
    }

    public void setCapcity(int capcity) {
        this.capcity = capcity;
    }

    public boolean isDirection() {
        return direction;
    }

    public void setDirection(boolean direction) {
        this.direction = direction;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getLastestToFloor() {
        return lastestToFloor;
    }

    public void setLastestToFloor(int lastestToFloor) {
        this.lastestToFloor = lastestToFloor;
    }

    public int getLastestCallFloor() {
        return lastestCallFloor;
    }

    public void setLastestCallFloor(int lastestCallFloor) {
        this.lastestCallFloor = lastestCallFloor;
    }
}
