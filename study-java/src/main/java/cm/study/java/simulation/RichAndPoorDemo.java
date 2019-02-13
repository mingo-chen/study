package cm.study.java.simulation;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * 在一个房间里有N个, 每个人有M元钱
 * 每个人随机给某个人1元钱
 * 一段时间后, 整个房间内金钱的分布
 */
public class RichAndPoorDemo {

    private List<People> peoples = Lists.newArrayList();

    private int N = 10;

    private int M = 100;

    public void simulation() {
        Context context = new Context();
        context.setPeopleList(peoples);

        for(int n = 0; n < N; n++) {
            People people = new People(M, n, context);
            peoples.add(people);

            people.start();
        }

    }

    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        RichAndPoorDemo demo = new RichAndPoorDemo();
        demo.simulation();
    }
}
