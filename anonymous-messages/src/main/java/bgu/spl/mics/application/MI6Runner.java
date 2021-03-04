package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.publishers.TimeService;
import bgu.spl.mics.application.subscribers.Intelligence;
import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Moneypenny;
import bgu.spl.mics.application.subscribers.Q;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOError;
import java.io.Reader;
import java.util.*;

//import com.sun.org.apache.xpath.internal.operations.String;

/**
 * This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class MI6Runner {
    public static void main(String[] args) {
        Inventory inventory = Inventory.getInstance();
        Squad squad = Squad.getInstance();
        List<MissionInfo> listOfMissionInfo;
        Diary diary = Diary.getInstance();
        LinkedList<Thread> threadList = new LinkedList<>();

        Gson gson = new Gson();
        try {
            Reader reader = new FileReader(args[0]);
            JsonFields jsonFields = gson.fromJson(reader, JsonFields.class);
            for (int p = 1; p <= jsonFields.services.M; p++) {
                M m = new M(p);
                threadList.add(new Thread(m, m.getName()));
            }
            for (int q = 1; q <= jsonFields.services.Moneypenny; q++) {
                Moneypenny mp = new Moneypenny(q);
                threadList.add(new Thread(mp, mp.getName()));
            }

            //reading json to jasonField class
            inventory.load(jsonFields.inventory); // updating inventory
            updateTheAgentAndSquad(jsonFields, squad); // updating squad and agents

            for (int i = 0; i < jsonFields.services.intelligence.length; i++) {
                listOfMissionInfo = makingMissionFile(i, jsonFields);
                Map<Integer, List<MissionInfo>> missionMapByTime = new HashMap<>();
                missionMapByTime = creatingMapForInt(listOfMissionInfo);
                Intelligence intelligence = new Intelligence(Integer.toString(i + 1), missionMapByTime);
                threadList.add(new Thread(intelligence, intelligence.getName()));

            }
            threadList.add(new Thread(new Q("Q"), "Q"));
            Thread time = new Thread(TimeService.createTimeService(jsonFields.services.time), "clock");
            time.start();

            for (Thread T : threadList) {
                T.start();
            }
            try {
                for (Thread j : threadList) j.join();
                time.join();
            } catch (InterruptedException ex) {

                ex.printStackTrace();
            }


        } catch (IOError | FileNotFoundException e) {
            e.printStackTrace();
        }
        inventory.printToFile(args[1]);
        diary.printToFile(args[2]);

    }


    private static Map<Integer, List<MissionInfo>> creatingMapForInt(List<MissionInfo> listOfMissionInfo) {
        Map<Integer, List<MissionInfo>> missionMapByTime = new HashMap<>();
        for (int l = 0; l < listOfMissionInfo.size(); l++) {
            missionMapByTime.putIfAbsent(listOfMissionInfo.get(l).getTimeIssued(), new LinkedList<>());
            missionMapByTime.get(listOfMissionInfo.get(l).getTimeIssued()).add(listOfMissionInfo.get(l));
        }
        return missionMapByTime;
    }

    private static List<MissionInfo> makingMissionFile(int i, JsonFields jsonFields) {
        List<MissionInfo> listOfMissionInfo = new LinkedList<>();
        for (int j = 0; j < jsonFields.services.intelligence[i].missions.length; j++) {

            MissionInfo missionInfo = new MissionInfo();
            //adding the serials
            List<String> SerTooInsert = new LinkedList<>();
            Collections.addAll(SerTooInsert, jsonFields.services.intelligence[i].missions[j].serialAgentsNumbers);
            missionInfo.setSerialAgentsNumbers(SerTooInsert);

            missionInfo.setDuration(jsonFields.services.intelligence[i].missions[j].duration); //adding the duration
            missionInfo.setGadget(jsonFields.services.intelligence[i].missions[j].gadget); //adding the gadget
            missionInfo.setMissionName(jsonFields.services.intelligence[i].missions[j].missionName); //adding the mission name
            missionInfo.setTimeExpired(jsonFields.services.intelligence[i].missions[j].timeExpired); //adding the time expired
            missionInfo.setTimeIssued(jsonFields.services.intelligence[i].missions[j].timeIssued); //adding the time issued
            listOfMissionInfo.add(missionInfo);

        }
        return listOfMissionInfo;
    }

    public static void updateTheAgentAndSquad(JsonFields jsonFields, Squad squad) {
        Agent[] agents = new Agent[jsonFields.squad.length];
        for (int i = 0; i < jsonFields.squad.length; i++) {
            Agent agent = new Agent();
            agent.setName(jsonFields.squad[i].name);
            agent.setSerialNumber(jsonFields.squad[i].serialNumber);
            agents[i] = agent;
        }
        squad.load(agents);
    }
}
