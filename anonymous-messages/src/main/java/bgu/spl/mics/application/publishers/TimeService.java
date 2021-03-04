package bgu.spl.mics.application.publishers;

import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.Publisher;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;

import static java.lang.Thread.sleep;


/**
 * TimeService is the global system timer There is only one instance of this Publisher.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other subscribers about the current time tick using {@link TickBroadcast}.
 * This class may not hold references for objects which it is not responsible for.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */

public class TimeService extends Publisher {
    private MessageBrokerImpl messageBroker;
    private int Tick;
    private int duration;
    private static boolean isInitit;
    private static TimeService instance;

    public static TimeService getInstance() {
        if (isInitit) {
            return null;
        } else {
            return instance;
        }

    }

    public static TimeService createTimeService(int duration) {
        instance = new TimeService(duration);
        return instance;
    }

    private TimeService(int duration) {
        super("TimeService");
        messageBroker = (MessageBrokerImpl) MessageBrokerImpl.getInstance();
        Tick = 0;
        this.duration = duration;
        isInitit = true;
    }

    public int getTick() {
        return Tick;
    }

    @Override
    protected void initialize() {

        while (Tick < duration) {
            messageBroker.sendBroadcast(new TickBroadcast(Tick));
            try {
                //System.out.println(Tick);
                sleep(100);
                Tick++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        messageBroker.sendBroadcast(new TickBroadcast(0));
        messageBroker.sendBroadcast(new TerminateBroadcast());

    }


    @Override
    public void run() {
        initialize();
    }

}
