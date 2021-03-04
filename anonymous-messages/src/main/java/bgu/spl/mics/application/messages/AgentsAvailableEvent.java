package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.Pair;
import bgu.spl.mics.Future;

import java.util.List;

public class AgentsAvailableEvent implements Event<Pair<Integer,List<String>>> {
    private List<String> availableAgents;
    public Future<Integer> duration;
    public AgentsAvailableEvent(List<String> availableAgents){
        this.availableAgents=availableAgents;
        this.duration = new Future<>();
    }
    public List<String> getAvailableAgents(){return availableAgents;}
}
