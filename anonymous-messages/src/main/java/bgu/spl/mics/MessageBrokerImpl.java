package bgu.spl.mics;

import java.util.Queue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingDeque;


/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBrokerImpl implements MessageBroker {

    private ConcurrentHashMap<Subscriber, BlockingDeque<Message>> queueMap;
    private ConcurrentHashMap<Class<? extends Message>, ConcurrentLinkedQueue<Subscriber>> msgMap;
    private ConcurrentHashMap<Event, Future> futureResults;
    private MessageBrokerImpl() {
        queueMap = new ConcurrentHashMap<>();
        msgMap = new ConcurrentHashMap<>();
        futureResults = new ConcurrentHashMap<>();
    }

    /**
     * Retrieves the single instance of this class.
     */

    public static MessageBroker getInstance() {
        return SingeltonHolder.instance;
    }

    @Override
    public <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber m) {
        msgMap.putIfAbsent(type, new ConcurrentLinkedQueue<>());
        msgMap.get(type).add(m);
    }

    @Override
    public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
        msgMap.putIfAbsent(type, new ConcurrentLinkedQueue<>());
        msgMap.get(type).add(m);
    }

    @Override
    public <T> void complete(Event<T> e, T result) {
        futureResults.get(e).resolve(result);
    }

    @Override
    public void sendBroadcast(Broadcast b) {
        Queue<Subscriber> broadcast = msgMap.get(b.getClass()); //return subscriber queue that has all the subscribers who assigned to b broadcast
        if (broadcast != null) {
            for (Subscriber s : broadcast) { //adding the b broadcast to every subscriber in queueMap
                synchronized (s) {
                    if (queueMap.containsKey(s)) {
                        queueMap.get(s).add(b);
                    }
                }
            }
        }
    }

    @Override
    public <T> Future<T> sendEvent(Event<T> e) {

        Future<T> future = new Future<>();
        ConcurrentLinkedQueue<Subscriber> q = msgMap.get(e.getClass());
        Subscriber s;
        if (q == null) {
            return null;
        }

        synchronized (e.getClass()) {
            s = q.poll();//we tookout the first subscriber in the round and put in parameter
            if (s == null) {
                return null;
            }
            q.add(s);//we added it back at the end of the round
        }

        synchronized (s) {
            if (queueMap.containsKey(s)) {
                futureResults.put(e, future);
                queueMap.get(s).add(e);
            }
        }

        return futureResults.get(e);
    }

    @Override
    public void register(Subscriber m) {
        queueMap.computeIfAbsent(m, l -> new LinkedBlockingDeque<>());
    }

    @Override
    public void unregister(Subscriber m) {

        msgMap.forEach((msg, subscribers) -> {
            synchronized (msg) {
                subscribers.remove(m);
            }
        });

        BlockingDeque<Message> allEventThatRegistered;

        synchronized (m) {
            allEventThatRegistered = queueMap.remove(m);
        }

        if (allEventThatRegistered != null) {
            for (Message mess : allEventThatRegistered) {
                if (futureResults.containsKey(mess) && !futureResults.get(mess).isDone()) {
                    futureResults.get(mess).resolve(null);
                }
            }
        }
    }

    @Override
    public Message awaitMessage(Subscriber m) throws InterruptedException {

        if (!queueMap.containsKey(m)) {
            throw new InterruptedException();
        } else {
            return queueMap.get(m).take();
        }

    }

    private static class SingeltonHolder {
        private static MessageBrokerImpl instance = new MessageBrokerImpl();
    }

}
