package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.Report;

import java.util.LinkedList;
import java.util.List;

public class JavaToJson {
    public List<ReportPublicFields> allReports;
    public int totalMissions;

    public JavaToJson(List<Report> allReports, int total) {
        this.allReports=new LinkedList<>();
        for (int i = 0; i < allReports.size(); i++) {
            this.allReports.add(new ReportPublicFields(allReports.get(i)));
        }
        this.totalMissions = total;
    }

    public class ReportPublicFields {
        public String missionName;
        public int M;
        public int moneyPenny;
        public List<String> agentsSerialNumbers;
        public List<String> agentsNames;
        public String gadgetName;
        public int timeIssued;
        public int QTime;
        public int timeCreated;

        public ReportPublicFields(Report report) {
            missionName = report.getMissionName();
            M = report.getM();
            moneyPenny = report.getMoneypenny();
            agentsSerialNumbers = report.getAgentsSerialNumbersNumber();
            agentsNames = report.getAgentsNames();
            gadgetName = report.getGadgetName();
            timeIssued = report.getTimeIssued();
            QTime = report.getqTime();
            timeCreated = report.getTimeCreated();

        }
    }
}