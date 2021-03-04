package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.util.List;

public class SendAgentEvent implements Event {
    List<String> availableAgents;
    int timeOfMission;

    public SendAgentEvent(List<String> serials, int timeIssued) {
        availableAgents = serials;
        this.timeOfMission = timeIssued;
    }

    public List<String> getAgents() {
        return availableAgents;
    }

    public int getTimeOfMission() {
        return timeOfMission;
    }

}
