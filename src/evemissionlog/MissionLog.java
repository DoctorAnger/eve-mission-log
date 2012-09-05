package evemissionlog;

import java.util.Date;

public class MissionLog {
    private long duration;
    private Date timestamp;
    private boolean complete;

    public MissionLog()
    {
        duration = 0;
        complete = false;
    }
    public MissionLog(Date timestamp, boolean complete)
    {
        this.timestamp = timestamp;
        this.complete = complete;
    }
    
    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
