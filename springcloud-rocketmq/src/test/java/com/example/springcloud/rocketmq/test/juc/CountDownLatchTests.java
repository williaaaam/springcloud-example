package com.example.springcloud.rocketmq.test.juc;

import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

/**
 * @author Williami
 * @description
 * @date 2022/1/25
 */
public class CountDownLatchTests {

    public static void main(String[] args) throws InterruptedException {
        // 闭锁不区分公平和非公平锁
        CountDownLatch count = new CountDownLatch(10);
        IntStream.rangeClosed(1, 11).forEach(i -> {
            new Thread(() -> {
                log(i);
                // 多次调用countDown, 只要getState() = 0,tryReleaseShared()会直接返回false，造成的结果就是不用等到permits个线程到来便可执行下面的await（）而不会造成阻塞
                count.countDown();
                count.countDown();
            }, "Thread-Q-" + i).start();
        });
        // getState() == 0, tryAcquireShared = 1, 表示没有持有锁的线程
        // 多次调用await()效果还是一样的
        count.await();
        count.await();
        System.out.println("所有线程执行完毕");

    }

    static void log(int i) {
        System.out.println(Thread.currentThread().getName() + " execute");
    }


}
