package cm.study.java.simulation;

import com.google.common.collect.Lists;

import java.util.List;

public class People2 extends Thread {

    private int money;

    private List<Message> messages;

    private Context context;

    public People2(int money, Context context) {
        this.money = money;
        messages = Lists.newArrayList();
        this.context = context;
    }

    public void post(Message message) {
        messages.add(message);
    }

    @Override
    public void run() {
        while (true) {

        }
        //
    }

    public static class Message {

    }
}
