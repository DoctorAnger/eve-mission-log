package evemissionlog;

import java.util.ArrayList;
import java.util.List;

public class MissionType {
    
    private String name;
    private List<MissionLog> missions;

    public MissionType(String name) {
        this.name = name;
        missions = new ArrayList<MissionLog>();
    }
    
    public List<MissionLog> getMissions() {
        return missions;
    }

    public void setMissions(List<MissionLog> missions) {
        this.missions = missions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Displays a general report based on whether or not the mission gets accepted when offered
     * @return 
     */
    public String display()
    {
        long totalTime = 0;
        int timeTotal = 0;
        int totalCompletions = 0;
        int totalOffers = 0;
        for(MissionLog missionLog : missions)
        {
            if ((missionLog.getDuration() >= 4*60000) && (missionLog.getDuration()) <= 30*60000 && missionLog.isComplete())
            {
                totalTime += missionLog.getDuration();
                timeTotal++;
            }
            if (missionLog.isComplete())
            {totalCompletions++;}
            totalOffers++;
        }
        if (totalTime > 0 && timeTotal > 0)
        {return name + "\n " + totalCompletions + "\\" + totalOffers + " completion" + (totalCompletions == 1 ? "" : "s") + ", Avg Duration: " + (totalTime/timeTotal/60000) + " minutes\n";}
        else if (totalCompletions > 0)
        {return name + "\n " + totalCompletions + "\\" + totalOffers + " completion" + (totalCompletions == 1 ? "" : "s") + "\n";}
        else
        {return name + "\n Offered " + totalOffers + " time" + (totalOffers == 1 ? "" : "s") + "\n";}
    }
}
