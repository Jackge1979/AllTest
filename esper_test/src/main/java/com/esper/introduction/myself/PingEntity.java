package com.esper.introduction.myself;

import java.io.Serializable;

/**
 * Created by lcc on 2018/9/25.
 */
public class PingEntity implements Serializable {

    private int counts;
    private long timeStamp;

    public int getCounts() {
        return counts;
    }

    public void setCounts(int counts) {
        this.counts = counts;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
