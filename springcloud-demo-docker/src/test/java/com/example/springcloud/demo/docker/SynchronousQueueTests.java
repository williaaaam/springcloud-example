package com.example.springcloud.demo.docker;


import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Williami
 * @description
 * @date 2021/12/13
 */
public class SynchronousQueueTests {

    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 10, 60L, TimeUnit.SECONDS, new SynchronousQueue(), new ThreadPoolExecutor.AbortPolicy());

    @Test
    public void testThreadPoolWithSyncQueue() throws InterruptedException {
        StopWatch watchDog = new StopWatch();
        watchDog.start();
        for (int i = 0; i < 20; i++) {
            int j = i;
            threadPoolExecutor.execute(() -> {
                int sleepInMills = ThreadLocalRandom.current().nextInt(500);
                try {
                    TimeUnit.MILLISECONDS.sleep(sleepInMills);
                    System.out.println(Thread.currentThread().getName() + " -- " + j);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        watchDog.stop();
        System.out.println("costs " + watchDog.getTotalTimeMillis() + " ms");
        System.out.println(threadPoolExecutor.getActiveCount());
        threadPoolExecutor.awaitTermination(3L, TimeUnit.SECONDS);
    }

    @Test
    public void testThreadPoolCapacity() {
        final int COUNT_BITS = Integer.SIZE - 3;
        // 11111111111111111111111111111 29个1
        System.out.println(Integer.toBinaryString((1 << COUNT_BITS) - 1));
        // 11100000000000000000000000000000 32个1
        System.out.println(Integer.toBinaryString((-1 << COUNT_BITS)).length());
    }

}
