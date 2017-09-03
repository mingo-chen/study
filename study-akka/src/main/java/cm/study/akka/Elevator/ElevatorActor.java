package cm.study.akka.Elevator;

import akka.actor.UntypedActor;
import cm.study.common.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 电梯
 */
public class ElevatorActor extends UntypedActor {

//    private static Logger ILOG = LoggerFactory.getLogger(ElevatorActor.class);

    public static final int CAPACITY = 1200;    // 电梯最大容量

    public static final long speed = 200;        // 电梯运行速度, 单位: 毫秒

    private int maxFloor;

    private List<Elevator> elevators;

    public ElevatorActor(int maxFloor, int number) {
        this.maxFloor = maxFloor;
        elevators = new ArrayList<>(number);

        for(int i = 0; i < number; i++) {
            Elevator elevator = new Elevator();
            elevator.setName("elevator-" + i);
            elevator.setCurFloor(0);
            elevator.setDirection(0);
            elevator.setCapacity(CAPACITY);
            elevator.setPassengers(new ArrayList<>());
            elevators.add(elevator);
//            ILOG.info("电梯初始化: {}", elevator);
            System.out.println(String.format("电梯初始化: %s", elevator));
        }
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        System.out.println(String.format("ElevatorActor receive message: %s", message));

        if(message instanceof Message.WaitElevator) {
            // 等待电梯消息, 找一台空闲的电梯去指定楼层
            Elevator elevator = selectElevator((Message.WaitElevator) message);
            if(elevator!=null && elevator.direction == 0) {
                // 电梯是从静止 -> 运动
                elevator.direction = 1;
//                ILOG.info("电梯被激活: {}", elevator);
                System.out.println(String.format("电梯被激活: %s", elevator));
                Message.ArriveFloor  arriveFloor = new Message.ArriveFloor();
                arriveFloor.elevator = elevator;
                ElevatorApp.DISPATCHER.tell(arriveFloor, getSelf());

            } else {
                // 电梯本身是运行状态

            }

        } else if (message instanceof Message.ElevatorRun) {
            run((Message.ElevatorRun) message);

        } else if (message instanceof Message.ElevatorStop) {
            stop((Message.ElevatorStop) message);

        } else {
            unhandled(message);
        }
    }

    /**
     * 电梯运行
     * @param message
     */
    void run(Message.ElevatorRun message) {
        // 判断电梯里有无人出去, 如果有, 则开门
        ThreadUtils.sleep(speed);

        // 电梯在楼层间移动一位
        Elevator elevator = message.elevator;
        if (elevator.direction > 0) {
            // 向上运行
            if (elevator.curFloor < maxFloor) {
                elevator.curFloor++;
            } else {
                elevator.direction = -elevator.direction;
                elevator.curFloor--;
            }
        } else {
            if (elevator.curFloor > 0) {
                elevator.curFloor--;
            } else {
                elevator.direction = -elevator.direction;
                elevator.curFloor++;
            }
        }

//        ILOG.info("电梯运行中: {}", message);
        System.out.println(String.format("电梯运行中: %s", message));
        Message.ArriveFloor arriveFloor = new Message.ArriveFloor();
        arriveFloor.elevator = elevator;
        ElevatorApp.DISPATCHER.tell(arriveFloor, getSelf());
    }

    void stop(Message.ElevatorStop message) {
        message.elevator.direction = 0;
//        ILOG.info("电梯停止运行: {}", message);
        System.out.println(String.format("电梯停止运行: %s", message));
    }

    /**
     * 选择电梯
     */
    Elevator selectElevator(Message.WaitElevator waitElevator) {
        Elevator selected = null;
        int minDistance = maxFloor;
        int waitDirection = waitElevator.endFloor > waitElevator.startFloor? 1 : -1;

        for (Elevator elevator : elevators) {
            if (waitDirection * elevator.getDirection() == -1) {
                // 方向不同, 直接过滤掉
                continue;
            }

            if (elevator.getDirection() == 0) {
                // 电梯静止的
                if(minDistance > (waitElevator.startFloor - elevator.curFloor)) {
                    minDistance = waitElevator.startFloor - elevator.curFloor;
                    selected = elevator;
                }

            } else {
                // 同向, 且电梯是运动的
                // 等待ArriveFloor事件, 自动进入电梯
            }
        }

        return selected;
    }

    public static class Elevator { // 电梯对象
        private String name;    // 电梯名字

        private int curFloor;   // 当前楼层, 默认0层

        private int direction;  // 运行方向, 1: 向上, 默认, -1: 向下, 0: 静止

        private int capacity;   // 容量

        private List<PassengerActor.Passenger> passengers;  // 当前乘客

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getCurFloor() {
            return curFloor;
        }

        public void setCurFloor(int curFloor) {
            this.curFloor = curFloor;
        }

        public int getDirection() {
            return direction;
        }

        public void setDirection(int direction) {
            this.direction = direction;
        }

        public int getCapacity() {
            return capacity;
        }

        public void setCapacity(int capacity) {
            this.capacity = capacity;
        }

        public List<PassengerActor.Passenger> getPassengers() {
            return passengers;
        }

        public void setPassengers(List<PassengerActor.Passenger> passengers) {
            this.passengers = passengers;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Elevator{");
            sb.append("name='").append(name).append('\'');
            sb.append(", curFloor=").append(curFloor);
            sb.append(", direction=").append(direction);
            sb.append(", capacity=").append(capacity);
            sb.append(", passengers=").append(passengers);
            sb.append('}');
            return sb.toString();
        }
    }

}
