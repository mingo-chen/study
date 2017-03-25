package cm.study.rxjava.eds;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 人
 * Created by chenming on 2017/2/26.
 */
public class People {

    private static final Logger ILOG = LoggerFactory.getLogger( People.class );

    /** 名字 */
    private String name;

    /** 体重 */
    private int weight;

    /** 起始楼层 */
    private int startFloor;

    /** 目标楼层 */
    private int targetFloor;

    /** 开始等候时间 */
    public long startTime;

    /** 进入时间 */
    private long inTime;

    /** 出去时间 */
    private long outTime;

    /**
     * 乘坐的电梯
     * 用于防并发
     */
    private volatile Elevator elevator;

    /**
     * 判断是向上, 还是向下
     * @return
     */
    public boolean isUp() {
        return targetFloor > startFloor;
    }

    /**
     * 进电梯
     */
    public void inElevator(Elevator elevator) {
        if(this.elevator != null) {
            throw new RuntimeException("[" + name + "]已经进入电梯");
        }

        this.elevator = elevator;
        elevator.setLastestToFloor(getTargetFloor());
        setInTime(System.currentTimeMillis());
        EdsUtils.sleep(100); // 进电梯耗时
        ILOG.info("[{}]进电梯!!!,从楼层[{}] 到 [{}], 等候耗时[{}] ms", name, startFloor, targetFloor, (inTime-startTime));
    }

    /**
     * 出电梯
     */
    public void outElevator() {
        setOutTime(System.currentTimeMillis());
        EdsUtils.sleep(100); // 出电梯耗时
        ILOG.info("[{}]出电梯!!! 完成坐电梯任务! 从楼层[{}] 到 [{}], 运行耗时[{}] ms", name, startFloor, targetFloor, (outTime-inTime));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getStartFloor() {
        return startFloor;
    }

    public void setStartFloor(int startFloor) {
        this.startFloor = startFloor;
    }

    public int getTargetFloor() {
        return targetFloor;
    }

    public void setTargetFloor(int targetFloor) {
        this.targetFloor = targetFloor;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getInTime() {
        return inTime;
    }

    public void setInTime(long inTime) {
        this.inTime = inTime;
    }

    public long getOutTime() {
        return outTime;
    }

    public void setOutTime(long outTime) {
        this.outTime = outTime;
    }

    @Override
    public String toString() {
        return "People{" +
                "name='" + name + '\'' +
                ", weight=" + weight +
                ", startFloor=" + startFloor +
                ", targetFloor=" + targetFloor +
                ", startTime=" + startTime +
                ", inTime=" + inTime +
                ", outTime=" + outTime +
                ", elevator=" + elevator +
                '}';
    }
}
