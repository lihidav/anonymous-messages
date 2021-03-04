package bgu.spl.mics.application;

public class JsonFields {
    public String[] inventory;
    public Services services;
    public Squad[] squad;

    public class Services{
        public int M;
        public int Moneypenny;
        public Intelligence[] intelligence;
        public int time;

        public class Intelligence{
            public Mission[] missions;
            public class Mission{
                public String[] serialAgentsNumbers;
                public int duration;
                public String gadget;
                public String missionName;
                public int timeExpired;
                public int timeIssued;
            }
        }
    }

    public class Squad{
        public String name;
        public String serialNumber;
    }
}
