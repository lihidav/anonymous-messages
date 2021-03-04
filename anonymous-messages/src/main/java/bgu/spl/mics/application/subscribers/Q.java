package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.Pair;
import bgu.spl.mics.application.messages.GadgetAvailableEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Inventory;


/**
 * Q is the only Subscriber\Publisher that has access to the {@link bgu.spl.mics.application.passiveObjects.Inventory}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Q extends Subscriber {

	private Inventory inventory;
	private int Tick;

	public Q(String name) {
		super(name);
		inventory = Inventory.getInstance();
	}

	@Override
	protected void initialize() {
		subscribeBroadcast(TickBroadcast.class, time->{
				this.Tick=time.getTimeTick();
		});
		subscribeEvent(GadgetAvailableEvent.class, takegadget->{
				complete(takegadget, new Pair<>(inventory.getItem(takegadget.getGadget()), inventory.getTick()));
			});
		subscribeBroadcast(TerminateBroadcast.class, termin->{
			this.terminate();
		});
	}

}
