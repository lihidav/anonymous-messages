package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.MissionReceivedEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

import java.util.List;
import java.util.Map;

/**
 * A Publisher only.
 * Holds a list of Info objects and sends them
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Intelligence extends Subscriber {
	private int Time;
	private Map<Integer,List<MissionInfo>> missionMapByTime;


	public Intelligence(String name,Map<Integer,List<MissionInfo>> missionMapByTime) {
		super(name);
		this.missionMapByTime=missionMapByTime;
		this.Time=0;
	}

	@Override
	protected void initialize() {
		subscribeBroadcast(TickBroadcast.class, time->{
			this.Time=time.getTimeTick();
			if(this.missionMapByTime.containsKey(Time)) {
				for (int i = 0; i < this.missionMapByTime.get(Time).size();i++) {
					getSimplePublisher().sendEvent(new MissionReceivedEvent(this.missionMapByTime.get(Time).get(i)));
				}
			}
		});
		subscribeBroadcast(TerminateBroadcast.class,termin->{
			this.terminate();
		});
	}



}


