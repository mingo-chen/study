package cm.study.akka.Elevator;

import akka.actor.ActorLogging;
import akka.actor.UntypedActor;
import cm.study.common.ThreadUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 乘客
 *  乘坐消息
 *  进入
 */
public class PassengerActor extends UntypedActor {

//    private static Logger ILOG = LoggerFactory.getLogger(PassengerActor.class);

    public static final Random RAND = new Random();

    public static final AtomicInteger COUNTER = new AtomicInteger();

    private static ExecutorService executorService = Executors.newFixedThreadPool(1);

    @Override
    public void onReceive(Object message) throws Throwable {
        System.out.println(String.format("PassengerActor receive message: %s", message));

        if (message instanceof String) {
//            ILOG.info("[{}] {}", getSelf().path().name(), message);
//            log().info("[%s] %s", getSelf().path().name(), message);
            System.out.println(String.format("[%s] %s", getSelf().path().name(), message));

            String msg = (String) message;
            if(StringUtils.equals(msg, "START-WORLD")) {
//                ILOG.info("启动世界");
//                log().info("启动世界");
                System.out.println("启动世界");

                buildRandomPassenger();
            }

        } else {
            unhandled(message);
        }

    }

    /**
     * 构造随机产生乘坐事件
     */
    void buildRandomPassenger() {
        executorService.submit(() -> {

            for(int index = 0; index < 2; index++) {
                Passenger passenger = Passenger.custom(ElevatorApp.MAX_FLOORS);
//                ILOG.info("来了位乘客: {}", passenger);
//                log().info("来了位乘客: %s", passenger);
                System.out.println(String.format("来了位乘客: %s", passenger));
                ElevatorApp.DISPATCHER.tell(passenger, getSelf());

                ThreadUtils.sleep(RAND.nextInt(200) + 200);
            }

        });
    }

    public static final class Passenger {
        public String name;        // 乘客名, 为了日志输出

        public int startFloor;     // 开始楼层

        public int endFloor;       // 到达楼层

        public int weight;         // 重量, 单位kg

        public long rideTime;      // 乘坐时间

        public long inTime;        // 进入电梯时间
        public long outTime;       // 走出电梯时间

        public static Passenger custom(int maxFloors) {
            Passenger passenger = new Passenger();
            passenger.name = "passenger-" + COUNTER.addAndGet(1);
            passenger.startFloor = RAND.nextInt(maxFloors);
            do {
                passenger.endFloor = RAND.nextInt(maxFloors);
            } while (passenger.endFloor == passenger.startFloor);
            passenger.weight = RAND.nextInt(50) + 30;
            passenger.rideTime = System.currentTimeMillis();
            passenger.inTime = -1;
            passenger.outTime = -1;

            return passenger;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder(name);
            sb.append("从 ").append(startFloor);
            sb.append(" 楼到 ").append(endFloor);
            sb.append(" 楼去, 乘坐时间=").append(rideTime);
            sb.append(", 进入时间=").append(inTime);
            sb.append(", 走出时间=").append(outTime);
            return sb.toString();
        }
    }
}
