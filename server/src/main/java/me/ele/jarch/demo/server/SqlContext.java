package me.ele.jarch.demo.server;

import io.netty.channel.Channel;

/**
 * Created by bulu on 16/3/14.
 */
public class SqlContext {
    private String sqlString;
    private String result = null;
    private Channel clientChannel;

    private long start;
    private long end;

    public Scheduler scheduler;

    public SqlContext(Scheduler scheduler, String sqlString) {
        this.scheduler = scheduler;
        this.sqlString = sqlString;
    }

    public String getSqlString() {
        return sqlString;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public String getResult() {
        return result;
    }

    public Channel getClientChannel() {
        return clientChannel;
    }

    public long getEnd() {
        return end;
    }

    public long getStart() {
        return start;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public void setSqlString(String sqlString) {
        this.sqlString = sqlString;
    }
}
