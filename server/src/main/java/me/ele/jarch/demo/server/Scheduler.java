package me.ele.jarch.demo.server;

import io.netty.buffer.Unpooled;
import me.ele.jarch.demo.fakedb.FakeServer;
import me.ele.jarch.demo.fakedb.FakeTask;
import me.ele.jarch.demo.session.ServerSession;
import me.ele.jarch.demo.util.SqlSemaphore;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.stream.IntStream;

/**
 * Created by bulu on 16/3/14.
 */
public class Scheduler {
    Logger logger = Logger.getLogger(Scheduler.class);
    private LinkedTransferQueue<SqlContext> queue = new LinkedTransferQueue<>();
    private SqlSemaphore semaphore = new SqlSemaphore(2);
    private final List<Worker> workers = new ArrayList<Worker>();
    FakeServer dbsession = new FakeServer(100);
    //private ServerSession session = new ServerSession();

    private int workerCount;

    public Scheduler(int workerCount) {
        this.workerCount = workerCount;
        //session.start("127.0.0.1", 9980);
        IntStream.range(0, workerCount).forEach(i -> {
            Worker worker = new Worker();
            worker.setName("Worker-" + i);
            workers.add(worker);
        });
        dbsession.start();
    }

    public int getWorkerCount() {
        return workerCount;
    }

    public void start() {
        logger.info("Scheduler init...");
        workers.forEach(worker -> {worker.start();});
    }

    private class Worker extends Thread{
        @Override
        public void run() {
            while(true) {
                try {
                    semaphore.acquire();
                    SqlContext sql = queue.take();
                    dbsession.enqueue(new FakeTask("[ " + System.currentTimeMillis() + " ]", 100, sql));
                    semaphore.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void enqueue(SqlContext ctx) {
        this.queue.offer(ctx);
    }
}
