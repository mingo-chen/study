package cm.study.rxjava.eds;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 电梯调度系统
 * 一栋大厦有N层楼, 现在有E部电梯
 * 模拟电梯调度系统
 *
 * 对于人的行为有:
 *  1) 站在电梯前, 选择上/下
 *  2) 进入电梯后, 选择目的楼层
 *
 * 对于电梯行为有:
 *  1) 根据当前的用户需求, 选择一台合适的电梯
 *  2) 在指定合适的位置停下
 *
 * Created by chenming on 2017/2/26.
 */
public class DispatchSystem implements Runnable {

    private static final Logger ILOG = LoggerFactory.getLogger( DispatchSystem.class );

    /** 总电梯台数 */
    public static final int TOTAL_ELEVATORS = 3;

    /** 总楼层 */
    public static final int TOTAL_FLOORS = 7;

    public static final Random RNAD = new Random();

    /** 电梯 */
    private List<Elevator> elevators;

    /** 向上等待的人群队列 */
    private volatile List<People> up_wait_peoples = new ArrayList<>();

    /** 向下等待的人群队列 */
    private volatile List<People> down_wait_peoples = new ArrayList<>();

    public static void main(String[] args) {
        Thread system = new Thread(new DispatchSystem());
        system.start();

    }

    /**
     * 初始化电梯
     */
    public DispatchSystem() {
        elevators = new ArrayList<Elevator>();
        for(int index = 0; index < TOTAL_ELEVATORS; index++) {
            Elevator elevator = new Elevator("电梯_" + index,1, TOTAL_FLOORS);
            elevator.setContext(this);
            elevators.add(elevator);
            elevator.start();
        }

        ILOG.info("初始化电梯完成...");
    }

    @Override
    public void run() {
        for(;;) {
            comeOnPeopleEvent();

            notifyElevator();

            // 遍历
            EdsUtils.sleep(1000);
        }
    }

    void comeOnPeopleEvent() {
        if(RNAD.nextInt(100) < 30) { // 30%概率来一个人
            People people = PeopleFactory.build(TOTAL_FLOORS);
            // 选择一台电梯, 等候, 进电梯, 出电梯
            if(people.isUp()) { // 向上
                up_wait_peoples.add(people);
                ILOG.info("来了一个向上的人:{}", people);

            } else { // 向上
                down_wait_peoples.add(people);
                ILOG.info("来了一个向下的人:{}", people);

            }

        } else {
            ILOG.debug("没有来人...");
        }
    }

    /**
     * 通知电梯过来拉客
     */
    void notifyElevator() {
        for(People people : up_wait_peoples) {
            Elevator elevator = selectUpElevator(people.getStartFloor());
            if(null != elevator) {
                elevator.call(people.getStartFloor() < elevator.getFloor()? false : true, people.getStartFloor());
            }
        }

        for(People people : down_wait_peoples) {
            Elevator elevator = selectDownElevator(people.getStartFloor());
            if(null != elevator) {
                elevator.call(people.getStartFloor() < elevator.getFloor()? false : true, people.getStartFloor());
            }
        }
    }

    /**
     * 选择一台向上的电梯
     */
    Elevator selectUpElevator(int startFloor) {
        Elevator rt = null;
        int minDist = TOTAL_FLOORS;
        // 比较所有电梯到指定目标楼层的距离
        for(Elevator elevator : elevators) {
            int dist = elevator.distance(startFloor, true);
//            ILOG.info("电梯[{}]向上运行到 {} 层的距离是: {}, 电梯当前状态是:{}, 方向:{}, 楼层:{}",
//                    elevator.getName(), startFloor, dist, elevator.getStatus(), elevator.isDirection(), elevator.getFloor());
            if(dist < minDist) {
                rt = elevator;
                minDist = dist;
            }
        }

        return rt;
    }

    Elevator selectDownElevator(int startFloor) {
        Elevator rt = null;
        int minDist = TOTAL_FLOORS;
        // 比较所有电梯到指定目标楼层的距离
        for(Elevator elevator : elevators) {
            int dist = elevator.distance(startFloor, false);
//            ILOG.info("电梯[{}]向下运行到 {} 层的距离是: {}, 电梯当前状态是:{}, 方向:{}, 楼层:{}",
//                    elevator.getName(), startFloor, dist, elevator.getStatus(), elevator.isDirection(), elevator.getFloor());
            if(dist < minDist) {
                rt = elevator;
                minDist = dist;
            }
        }

        return rt;
    }

    public List<People> getUp_wait_peoples() {
        return up_wait_peoples;
    }

    public void setUp_wait_peoples(List<People> up_wait_peoples) {
        this.up_wait_peoples = up_wait_peoples;
    }

    public List<People> getDown_wait_peoples() {
        return down_wait_peoples;
    }

    public void setDown_wait_peoples(List<People> down_wait_peoples) {
        this.down_wait_peoples = down_wait_peoples;
    }
}
