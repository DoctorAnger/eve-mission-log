package evemissionlog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogParser {
    
    private HashMap<String,MissionType> missions;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");
    
    public LogParser()
    {
        missions = new HashMap<String,MissionType>();
    }
    
    /**
     * Parses a clipboard input and displays a cumulative report to System.out and returns it
     * @param s
     * @throws ParseException 
     */
    public String parse(String s) throws ParseException
    {
        String[] entries = s.split("\n");
        for(String entry : entries)
        {
            parseEntry(entry);
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("\n=====LOG=====\n");
        Iterator<String> keys = missions.keySet().iterator();
        while (keys.hasNext())
        {
            String key = keys.next();
            sb.append(missions.get(key).display());
        }
        sb.append(numberOfCompletions()).append(" completed, ").append(numberOfDeclines()).append(" declined\n");
        System.out.println(sb.toString());
        return sb.toString();
    }
    
    /**
     * Parses a single "line" of the clipboard and adds the mission to the data structure
     * @param s
     * @throws ParseException 
     */
    public void parseEntry(String s) throws ParseException
    {
        //Catches the date+time
        Pattern datePattern = Pattern.compile("\\d\\d\\d\\d\\.\\d\\d\\.\\d\\d\\s\\d\\d:\\d\\d");
        Matcher dateMatcher = datePattern.matcher(s);
        
        //Catches the "Completed" or "Failed" or "Declined" status
        Pattern statusPattern = Pattern.compile("Mission\\s(\\w*?)\\s");
        Matcher statusMatcher = statusPattern.matcher(s);
        
        //Catches the name
        Pattern namePattern = Pattern.compile("-\\s(.*)");
        Matcher nameMatcher = namePattern.matcher(s);
        
        if (dateMatcher.find() && statusMatcher.find() && nameMatcher.find())
        {
            String name = nameMatcher.group(1);
            Date date = dateFormat.parse(dateMatcher.group(0));
            boolean complete = statusMatcher.group(0).contains("Complete");
            addLog(name, date, complete);
        }
    }
    
    /**
     * Adds a single mission to the MissionLog structure for the given MissionType
     * and sets the duration according to the closest available date all the logs
     * 
     * @param name
     * @param endDate
     * @param complete 
     */
    public void addLog(String name, Date endDate, boolean complete)
    {
        MissionLog missionLog = new MissionLog(endDate, complete);
        if (!missions.containsKey(name))
        {missions.put(name,new MissionType(name));}
        
        missions.get(name).getMissions().add(missionLog);
        
        Date startDate = closestDate(endDate);
        if (startDate == null)
        {startDate = endDate;}
        else {System.out.println(startDate.getTime());}
        long duration = endDate.getTime() - startDate.getTime();
        missionLog.setDuration(duration);
    }
    
    /**
     * Finds the closest date in the mission log to the current date that is not
     * longer, for the purpose of finding the "last" mission complete time
     * @param endDate
     * @return 
     */
    public Date closestDate(Date endDate)
    {
        Date startDate = null;
        Iterator<String> keys = missions.keySet().iterator();
        while (keys.hasNext())
        {
            String key = keys.next();
            MissionType missionType = missions.get(key);
            List<MissionLog> list = missionType.getMissions();
            for(MissionLog missionLog : list)
            {
                if (missionLog.getTimestamp().before(endDate))
                {
                    if (startDate == null || startDate.before(missionLog.getTimestamp()))
                    {startDate = missionLog.getTimestamp();}
                }
                long testDuration = missionLog.getTimestamp().getTime() - endDate.getTime();
                if (testDuration > 0)
                {
                    if (testDuration < missionLog.getDuration() || missionLog.getDuration() == 0)
                    {missionLog.setDuration(testDuration);}
                }
            }
        }
        return startDate;
    }
    
    /**
     * Finds the total number of completion=true MissionLogs
     * @return 
     */
    public int numberOfCompletions()
    {
        int completions = 0;
        Iterator<String> keys = missions.keySet().iterator();
        while (keys.hasNext())
        {
            String key = keys.next();
            MissionType missionType = missions.get(key);List<MissionLog> list = missionType.getMissions();
            for(MissionLog missionLog : list)
            {
                if(missionLog.isComplete())
                    completions++;
            }
        }
        return completions;
    }
    
    /**
     * Finds the total number of completion=false MissionLogs
     * @return 
     */
    public int numberOfDeclines()
    {
        int declines = 0;
        Iterator<String> keys = missions.keySet().iterator();
        while (keys.hasNext())
        {
            String key = keys.next();
            MissionType missionType = missions.get(key);
            List<MissionLog> list = missionType.getMissions();
            for(MissionLog missionLog : list)
            {
                if(!missionLog.isComplete())
                    declines++;
            }
        }
        return declines;
    }
}
