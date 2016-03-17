package me.ele.jarch.demo.util;

import org.junit.Test;

import java.util.concurrent.Semaphore;

/**
 * Created by bulu on 16/3/15.
 */
public class SqlSemaphore {
    private ResizeableSemaphore semaphore = new ResizeableSemaphore(1);
    private int max = 1;

    public SqlSemaphore(int permits) {
        //int del = max - newMax;
        this.semaphore = new ResizeableSemaphore(permits);
    }

    public void add(int number) {
        this.semaphore.release(number);
    }

    public void del(int number) {
        this.semaphore.reducePermits(number);
    }

    public void acquire() {
        try {
            this.semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean tryAcquire() {
        return this.semaphore.tryAcquire();
    }

    public void release() {
        this.semaphore.release();
    }

    public int availablePermits() {
        return this.semaphore.availablePermits();
    }

    private final class ResizeableSemaphore extends Semaphore{
        public ResizeableSemaphore(int permits) {
            super(permits);
        }

        @Override
        protected void reducePermits(int reduction) {
            super.reducePermits(reduction);
        }
    }
}
