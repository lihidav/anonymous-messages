package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Future;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.Pair;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Report;


import java.util.List;

/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class M extends Subscriber {
    private Diary diary;
    private int Tick;
    private int intName;

    public M(int intName) {
        super("M" + intName);
        diary = Diary.getInstance();
        Tick = 0;
        this.intName = intName;

    }

    @Override
    protected void initialize() {
        subscribeBroadcast(TickBroadcast.class, time -> {
            this.Tick = time.getTimeTick();
        });

        subscribeEvent(MissionReceivedEvent.class, call -> {
            //System.out.println("hello");
            diary.incrementTotal();
            AgentsAvailableEvent checkAgents = new AgentsAvailableEvent(call.getMissionInfo().getSerialAgentsNumbers());
            Future<Pair<Integer, List<String>>> agantsAv = getSimplePublisher().sendEvent(checkAgents);
            if (agantsAv != null && agantsAv.get() != null) {
                //System.out.println("print");
                Future<Pair<Boolean, Integer>> gadgetAv = getSimplePublisher().sendEvent(new GadgetAvailableEvent(call.getMissionInfo().getGadget(), call.getMissionInfo().getTimeIssued(), call.getMissionInfo().getTimeExpired()));
                if (gadgetAv != null && gadgetAv.get() != null) {
                    //System.out.println("there is gadget");
                    if (agantsAv.get().getKey() != -1 & gadgetAv.get().getKey()) {
                        checkAgents.duration.resolve(call.getMissionInfo().getDuration());//send them
                        Report report = new Report(call.getMissionInfo(), intName, Tick);
                        report.setqTime(gadgetAv.get().getValue());
                        report.setMoneypenny(agantsAv.get().getKey());
                        diary.addReport(report);
                        //System.out.println("M: print " + call.getMissionInfo().getMissionName());
                        complete(call, null);
                        return;
                    }
                }
            }
            checkAgents.duration.resolve(-1);//release them
            complete(call, null);
        });


        subscribeBroadcast(TerminateBroadcast.class, termin -> {
            this.terminate();
        });

    }
}


