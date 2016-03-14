package serverdemo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by bulu on 16/3/14.
 */
public class Scheduler {
    private final List<Worker> workers = new ArrayList<Worker>();
    //private final BlockingQueue<String> queue = new ArrayBlockingQueue<String>();
    public void start() {
        workers.forEach(worker -> {
            worker.start();
        });
    }

    /*
    public void enqueue(String sql) throws InterruptedException {
        queue.put(sql);
    }
    */

    class Worker extends Thread {
        @Override
        public void run() {
            // send data to DB
        }
    }
}
