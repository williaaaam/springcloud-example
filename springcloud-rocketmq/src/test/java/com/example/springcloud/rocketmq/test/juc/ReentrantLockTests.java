package com.example.springcloud.rocketmq.test.juc;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Williami
 * @description
 * @date 2022/1/20
 */
public class ReentrantLockTests {

    ReentrantLock fairSync = new ReentrantLock(true);

    @Test
    public void testFairSync() throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            fairSync.lock();
            System.out.println("Thread1 - Running");
            fairSync.unlock();
        }, "Thread-1");


        Thread thread2 = new Thread(() -> {
            fairSync.lock();
            try {
                TimeUnit.SECONDS.sleep(3600L);
                System.out.println("Thread2 - Running");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            fairSync.unlock();
        }, "Thread-2");


        thread2.start();
        TimeUnit.MILLISECONDS.sleep(500);
        thread1.start();


        thread2.join();
        thread1.join();
    }
}
