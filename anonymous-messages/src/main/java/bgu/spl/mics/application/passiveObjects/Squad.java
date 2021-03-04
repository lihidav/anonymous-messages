package bgu.spl.mics.application.passiveObjects;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static java.lang.Thread.sleep;

/**
 * Passive data-object representing a information about an agent in MI6.
 * You must not alter any of the given public methods of this class. 
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Squad {

	private Map<String, Agent> agents;


	private static class SingeltonHolder{
		private static Squad instance = new Squad();
	}
	private Squad(){agents=new LinkedHashMap<>();
	}
	/**
	 * Retrieves the single instance of this class.
	 */
	public static Squad getInstance() {
		return SingeltonHolder.instance;
	}


	public String nameToSerial(String agentName){
		String numOfAgent=null;
		for(Map.Entry<String,Agent> entry : agents.entrySet()){
			if(entry.getValue().getName()==agentName){
				return entry.getKey();
			}
		}
		return numOfAgent;
	}

	/**
	 * Initializes the squad. This method adds all the agents to the squad.
	 * <p>
	 * @param agents 	Data structure containing all data necessary for initialization
	 * 						of the squad.
	 */
	public void load (Agent[] agents) {
		for (int i=0; i<agents.length; i++){
			this.agents.put(agents[i].getSerialNumber(),agents[i]);
		}
	}

	/**
	 * Releases agents.
	 */
	public synchronized void releaseAgents(List<String> serials){ //check synchronized
		for(int i =0; i< serials.size(); i++){
			this.agents.get(serials.get(i)).release();
		}
		notifyAll();
	}

	/**
	 * simulates executing a mission by calling sleep.
	 * @param time   milliseconds to sleep
	 */
	public synchronized void sendAgents(List<String> serials, int time){
		try {
			sleep(100*time);
		}catch (InterruptedException e){e.printStackTrace();}
		releaseAgents(serials);
		}

	/**
	 * acquires an agent, i.e. holds the agent until the caller is done with it
	 * @param serials   the serial numbers of the agents
	 * @return ‘false’ if an agent of serialNumber ‘serial’ is missing, and ‘true’ otherwise
	 */
	public synchronized boolean getAgents(List<String> serials) throws InterruptedException {
		for(int i =0; i< serials.size(); i++){
			if(!agents.containsKey(serials.get(i))){
				return false;
			}
		}

		for(int j =0; j< serials.size(); j++){
			while (!this.agents.get(serials.get(j)).isAvailable()){
				wait();
			}
			this.agents.get(serials.get(j)).acquire();
		}
		return true;

	}


    /**
     * gets the agents names
     * @param serials the serial numbers of the agents
     * @return a list of the names of the agents with the specified serials.
     */
    public List<String> getAgentsNames(List<String> serials){
    	List<String> stringName = new LinkedList<>();
        for(int i =0; i<serials.size();i++){
        	stringName.add(agents.get(serials.get(i)).getName());
		}
        return stringName;
    }

}
