/*package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Squad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SquadTest {
    Squad squad = new Squad();
    @BeforeEach
    public void setUp(){
        squad = Squad.getInstance();

    }

    @Test
    public void loadTest(){
        Agent agent1 = new Agent();
        agent1.setName("noa");
        agent1.setSerialNumber("007");
        Agent agent2 = new Agent();
        agent1.setName("lihi");
        agent1.setSerialNumber("008");
        Agent[] agents = {agent1, agent2} ;
        List<String> agentList = new LinkedList<>();
        agentList.add(agent1.getSerialNumber());
        agentList.add(agent2.getSerialNumber());


        assertFalse(squad.getAgents(agentList));
        squad.load(agents);
        assertTrue(squad.getAgents(agentList));
    }

    public void getInstanceTest(){
        Squad squad2 = Squad.getInstance();
        assertEquals(squad,squad2);
    }

    public void releaseAgentsTest(){
        
    }

}
*/