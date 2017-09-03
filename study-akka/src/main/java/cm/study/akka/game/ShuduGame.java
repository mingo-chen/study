package cm.study.akka.game;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenming on 2017/7/19.
 */
public class ShuduGame extends UntypedActor {

    private static Logger ILOG = LoggerFactory.getLogger(ShuduGame.class);

    private static ActorSystem GAME_WORLD = ActorSystem.create("game-world");
    private static ActorRef SHUDU = GAME_WORLD.actorOf(Props.create(ShuduGame.class), "shudu");

    public static void main(String[] args) {
        Map<String, Object> data = new HashMap<>();
        data.put("age", 18);
        data.put("name", "cm");
        SHUDU.tell(data, null);
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if(message instanceof Map) {
            ILOG.info("message: {}", message);
            ILOG.info("context: {}", getContext());
            ILOG.info("self: {}", getSelf());
            ILOG.info("sender: {}", getSender());

            ActorRef shudu = GAME_WORLD.actorOf(Props.create(Shudu.class), "sd");
            shudu.tell("success, akka", this.getSelf());
        } else if (message instanceof String) {
            ILOG.info("receive message: {}", message);

        } else {
            ILOG.warn("unhandled message: {}", message);
            unhandled(message);
        }

    }
}
