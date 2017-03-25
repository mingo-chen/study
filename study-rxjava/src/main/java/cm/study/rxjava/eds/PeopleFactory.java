package cm.study.rxjava.eds;

import java.util.Random;

/**
 * 人工厂
 * Created by chenming on 2017/2/26.
 */
public class PeopleFactory {

    public static final Random RAND = new Random();

    public static People build(int totalFloor) {
        People people = new People();
        people.setName("people_" + RAND.nextInt(10000));
        people.setWeight(40 + RAND.nextInt(40));
        people.setStartFloor(RAND.nextInt(totalFloor) + 1);
        people.setStartTime(System.currentTimeMillis());

        if(people.getStartFloor() == 1) { // 向上
            people.setTargetFloor(2 + RAND.nextInt(totalFloor - 1));
        } else if(people.getStartFloor() == totalFloor) { // 向下
            people.setTargetFloor(1 + RAND.nextInt(totalFloor - 1));
        } else {
            boolean up = RAND.nextBoolean();
            if (up) { // 向startFloor ~ totalFloor
                people.setTargetFloor(1 + people.getStartFloor() + RAND.nextInt(totalFloor - people.getStartFloor()));
            } else {
                people.setTargetFloor(1 + RAND.nextInt(people.getStartFloor() - 1));
            }
        }

        return people;
    }
}
