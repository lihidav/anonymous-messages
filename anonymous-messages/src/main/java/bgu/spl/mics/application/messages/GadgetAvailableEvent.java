package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import bgu.spl.mics.application.Pair;
public class GadgetAvailableEvent implements Event<Pair<Boolean, Integer>> {
    private String gadget;
    private int timeIssued;
    private int timeExpired;

    public GadgetAvailableEvent(String gadget,int timeIssued,int timeExpired){
        this.gadget=gadget;
        this.timeIssued=timeIssued;
        this.timeExpired=timeExpired;
    }

    public String getGadget() {
        return gadget;
    }

    public int getTimeIssued() {
        return timeIssued;
    }

    public int getTimeExpired() {
        return timeExpired;
    }
}