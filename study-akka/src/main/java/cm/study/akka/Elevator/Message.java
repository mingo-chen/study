package cm.study.akka.Elevator;

/**
 * Created by chenming on 2017/9/3.
 */
public interface Message {

    /**
     * 乘客等待电梯消息
     */
    class WaitElevator {
        public int startFloor;     // 开始楼层
        public int endFloor;       // 到达楼层
    }

    /**
     * 电梯运行消息
     */
    class ElevatorRun {
        public ElevatorActor.Elevator elevator;

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("ElevatorRun{");
            sb.append("elevator=").append(elevator);
            sb.append('}');
            return sb.toString();
        }
    }

    /**
     * 电梯停止消息
     */
    class ElevatorStop {
        public ElevatorActor.Elevator elevator;

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("ElevatorStop{");
            sb.append("elevator=").append(elevator);
            sb.append('}');
            return sb.toString();
        }
    }

    /**
     * 电梯到达楼层
     */
    class ArriveFloor {
        public ElevatorActor.Elevator elevator;

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("ArriveFloor{");
            sb.append("elevator=").append(elevator);
            sb.append('}');
            return sb.toString();
        }
    }
}
