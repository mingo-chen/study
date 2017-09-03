package cm.study.akka.Elevator;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;

/**
 *
 * Created by chenming on 2017/8/15.
 */
public class ElevatorApp {

    public static final int MAX_FLOORS = 10;
    public static final int ELEVATOR_NUMBER = 4;

    public static ActorSystem SYSTEM = ActorSystem.create("ELEVATOR-system", ConfigFactory.load());

    public static final ActorRef DISPATCHER = SYSTEM.actorOf(Props.create(DispatcherActor.class), "ELEVATOR-dispatcher");
    public static final ActorRef ELEVATOR = SYSTEM.actorOf(Props.create(ElevatorActor.class, MAX_FLOORS, ELEVATOR_NUMBER), "ELEVATOR-X");
    public static final ActorRef PASSENGER = SYSTEM.actorOf(Props.create(PassengerActor.class), "Passenger-X");

    public static void main(String[] args) {
        PASSENGER.tell("START-WORLD", null);
    }
}
