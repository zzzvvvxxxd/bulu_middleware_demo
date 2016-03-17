package me.ele.jarch.demo.fakedb;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.DelayQueue;
import java.util.stream.IntStream;

/**
 * Created by bulu on 16/3/15.
 */
public class FakeServer {
    private DelayQueue<FakeTask> queue = new DelayQueue<>();
    private int workerCount;
    private List<Worker> workers = new ArrayList<Worker>();

    public FakeServer(int workerCount) {
        this.workerCount = workerCount;
        IntStream.range(0, workerCount).forEach(i -> {
            Worker worker = new Worker();
            worker.setName("db-worker-" + i);
            workers.add(worker);
        });
    }

    public void start() {
        workers.stream().forEach(Thread::start);
    }

    public void enqueue(FakeTask task) {
        this.queue.add(task);
    }

    private class Worker extends Thread{
        @Override
        public void run() {
            while(true) {
                //System.out.println("trying to find new task");
                FakeTask task = null;
                try {
                    task = queue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                assert task != null;
                task.invoke();
                // end time
                // new Thread(task).start();
            }
        }
    }
}
