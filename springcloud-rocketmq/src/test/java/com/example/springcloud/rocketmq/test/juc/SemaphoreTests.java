package com.example.springcloud.rocketmq.test.juc;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @author Williami
 * @description
 * @date 2022/1/25
 */
public class SemaphoreTests {

    public static void main(String[] args) throws InterruptedException {
        // 共享锁
        Semaphore semaphore = new Semaphore(10, true);

        IntStream.rangeClosed(1, 20).forEach(i -> {
            if (semaphore.getQueueLength() > 0) {
                System.out.println("排队线程数=" + semaphore.getQueueLength() + ", 当前线程=" + Thread.currentThread().getName());
            }
            new Thread(() -> {
                try {
                    // 每次消耗一个许可
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName() + " " + "获得锁");
                    TimeUnit.SECONDS.sleep(2);
                    System.out.println(Thread.currentThread().getName() + " " + "释放锁锁");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }
            }, "Thread-Q-" + i).start();
        });

    }
}
