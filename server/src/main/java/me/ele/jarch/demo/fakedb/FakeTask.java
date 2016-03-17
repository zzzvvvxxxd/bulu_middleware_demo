package me.ele.jarch.demo.fakedb;

import me.ele.jarch.demo.server.SqlContext;
import org.apache.log4j.Logger;

import java.util.concurrent.*;

/**
 * Created by bulu on 16/3/15.
 */
public class FakeTask implements Delayed {
    Logger logger = Logger.getLogger(FakeTask.class);
    private String name;
    private long submitTime = 0;
    private SqlContext ctx;

    public FakeTask(String name, long durTime, SqlContext ctx) {
        this.name = name;
        this.ctx = ctx;
        this.submitTime = System.currentTimeMillis() + durTime;
        //this.sequenceNumber = sequencer.getAndIncrement();
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(submitTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed other) {
        if(other == this) {
            return 0;
        }
        if(other instanceof FakeTask) {
            FakeTask x = (FakeTask)other;
            return submitTime > x.submitTime ? 1 : (submitTime < x.submitTime ? -1 : 0);
        }
        long diff = (getDelay(TimeUnit.MILLISECONDS) - other.getDelay(TimeUnit.MILLISECONDS));
        return (diff == 0) ? 0 : ((diff < 0) ? -1 : 1);
    }

    public void invoke() {
        this.ctx.setResult("result");
    }
}
