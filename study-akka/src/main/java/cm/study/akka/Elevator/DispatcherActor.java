package cm.study.akka.Elevator;

import akka.actor.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 调度器
 *  1. 当前各电梯所处楼层
 *  2. 当前各电梯的容量
 *  3. 当前各电梯运行方向
 *
 * 消息:
 *  乘客乘坐消息
 *      1) 安排最近还有容量的电梯过来接客
 *      2) 通知所有电梯, 有乘客消息
 *
 *  电梯移动消息
 *      1) 电梯向上/向下移动
 */
public class DispatcherActor extends UntypedActor {

//    private static Logger ILOG = LoggerFactory.getLogger(DispatcherActor.class);

    /**
     * 每一楼层的等待队列
     * 下标代表楼层
     */
    private List<List<PassengerActor.Passenger>> waitPassengers = new ArrayList<>(ElevatorApp.MAX_FLOORS);

    public DispatcherActor() {
        init();
    }

    public void init() {
        for(int index = 0; index < ElevatorApp.MAX_FLOORS; index++) {
            List<PassengerActor.Passenger> passengers = new ArrayList<>();
            waitPassengers.add(passengers);
        }
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        System.out.println(String.format("DispatcherActor receive message: %s", message));

        if (message instanceof PassengerActor.Passenger) {
            // 乘坐消息, 把乘客放到等待列表中
            PassengerActor.Passenger passenger = (PassengerActor.Passenger) message;
            List<PassengerActor.Passenger> waitList = waitPassengers.get(passenger.startFloor);
            if (null == waitList) {
                waitList = new ArrayList<>();
                waitPassengers.add(passenger.startFloor, waitList);
            }
            waitList.add(passenger);

            // call ELEVATOR
            Message.WaitElevator waitElevator = new Message.WaitElevator();
            waitElevator.startFloor = passenger.startFloor;
            waitElevator.endFloor = passenger.endFloor;
            ElevatorApp.ELEVATOR.tell(waitElevator, getSelf());

        } else if(message instanceof Message.ArriveFloor) {
            Message.ArriveFloor arriveFloor = (Message.ArriveFloor) message;
            elevatorArriveFloor(arriveFloor);
//            System.out.println(String.format("电梯到达楼层: %s", arriveFloor));

            ElevatorActor.Elevator elevator = arriveFloor.elevator;
            if (elevator.getCurFloor() == 0 && elevator.getDirection() == -1) {
                // 电梯处于最底层
//                if(arriveFloor.elevator.getPassengers().isEmpty() && waitPeoples() == 0) {
//                    // 里面没人, 外面也没人, 就让其停下来
//                } else {
//                    //
//                }

                Message.ElevatorStop elevatorStop = new Message.ElevatorStop();
                elevatorStop.elevator = elevator;
                ElevatorApp.ELEVATOR.tell(elevatorStop, getSelf());

            } else {
                Message.ElevatorRun elevatorRun = new Message.ElevatorRun();
                if(changeDirection(elevator)) {
                    elevator.setDirection(-elevator.getDirection());
                }
                elevatorRun.elevator = elevator;
                ElevatorApp.ELEVATOR.tell(elevatorRun, getSelf());
            }

        } else {
            unhandled(message);
        }

    }

    /**
     * 判断当前电梯是否需要改变运行方向
     * @param elevator
     * @return
     */
    boolean changeDirection(ElevatorActor.Elevator elevator) {
        if(!elevator.getPassengers().isEmpty()) {
            return false;
        }

        if(elevator.getDirection() > 0) {
            // 向上运行, 且高层没有等待队列
            int totalWaits = 0;
            for(int index = elevator.getCurFloor(); index < ElevatorApp.MAX_FLOORS; index++) {
                List<PassengerActor.Passenger> waitList = waitPassengers.get(index);
                totalWaits += waitList==null? 0 : waitList.size();
            }

            return totalWaits == 0;

        } else {
            // 向下运行, 电梯里的人为空, 且底层没有等待队列高层有
            int totalHighWaits = 0;
            int totalLowWaits = 0;
            for(int index = 0; index < ElevatorApp.MAX_FLOORS; index++) {
                List<PassengerActor.Passenger> waitList = waitPassengers.get(index);
                if (waitList == null) {
                    continue;
                }

                if (index < elevator.getCurFloor()) {
                    totalLowWaits += waitList.size();
                } else {
                    totalHighWaits += waitList.size();
                }
            }

            return totalLowWaits == 0 && totalHighWaits > 0;
        }
    }

    /**
     * 统计处于等待队列中人数
     */
    public int waitPeoples() {
        int peoples = 0;
        for (List<PassengerActor.Passenger> waitList : waitPassengers) {
            peoples += waitList.size();
        }

        return peoples;
    }

    /**
     * 电梯到达楼层
     * 此楼层有无乘客下, 如果有, 就放乘客下
     * 此楼层有无等待列表, 如果有 就打开门
     */
    public void elevatorArriveFloor(Message.ArriveFloor message) {
        ElevatorActor.Elevator elevator = message.elevator;

        int totalWeight = 0;
        // 先下
        for (Iterator<PassengerActor.Passenger> iterator = elevator.getPassengers().iterator(); iterator.hasNext();) {
            PassengerActor.Passenger passenger = iterator.next();
            if (passenger.endFloor == elevator.getCurFloor()) {

                passenger.outTime = System.currentTimeMillis();
//                ILOG.info("出电梯: {}", passenger);
//                log().info("出电梯: " + passenger);
                System.out.println(String.format("出电梯: %s", passenger));
                iterator.remove();
            } else {
                totalWeight += passenger.weight;
            }
        }

        List<PassengerActor.Passenger> waitList = waitPassengers.get(elevator.getCurFloor());
        if (waitList != null) {
            // 后上
            for (Iterator<PassengerActor.Passenger> iterator = waitList.iterator(); iterator.hasNext(); ) {
                PassengerActor.Passenger passenger = iterator.next();
                int passengerDirection = passenger.endFloor > passenger.startFloor ? 1 : -1;
                if ((elevator.getDirection() * passengerDirection > 0) && (totalWeight + passenger.weight <= elevator.getCapacity())) {
                    // 同向 且无超载
                    iterator.remove();
                    passenger.inTime = System.currentTimeMillis();
                    elevator.getPassengers().add(passenger);
                }
            }
        }
    }

}
