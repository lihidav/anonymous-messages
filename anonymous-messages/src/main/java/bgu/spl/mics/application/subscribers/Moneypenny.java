package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.AgentsAvailableEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Squad;
import bgu.spl.mics.application.Pair;

import java.util.List;

/**
 * Only this type of Subscriber can access the squad.
 * Three are several Moneypenny-instances - each of them holds a unique serial number that will later be printed on the report.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Moneypenny extends Subscriber {
    private Squad squad;
    int Tick;
    int ID;

    public Moneypenny(int ID) {
        super("Moneypenny" + ID);
        squad = Squad.getInstance();
        this.ID = ID;
    }

    @Override
    protected void initialize() {
        subscribeBroadcast(TickBroadcast.class, time -> {
            if (time.getTimeTick() == 0) {
                terminate();
            } else {
                this.Tick = time.getTimeTick();
            }

        });

        subscribeEvent(AgentsAvailableEvent.class, agentAv -> {

            boolean isAgents = squad.getAgents(agentAv.getAvailableAgents());
            int nameIfCorrect = isAgents ? ID : -1;
            Pair<Integer, List<String>> theAnswer = new Pair<>(nameIfCorrect, agentAv.getAvailableAgents());

            complete(agentAv, theAnswer);
            if (isAgents) {
                Integer durationOfMission = agentAv.duration.get();
                if (durationOfMission <= 0) {
                    squad.releaseAgents(theAnswer.getValue());
                } else {
                    squad.sendAgents(theAnswer.getValue(), durationOfMission);
                }
            }
        });

        subscribeBroadcast(TerminateBroadcast.class, termin -> this.terminate());
    }
}
