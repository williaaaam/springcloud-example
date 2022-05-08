package com.example.springcloud.rocketmq.test.wait_notify;

import org.openjdk.jol.info.ClassLayout;

import java.util.stream.IntStream;

/**
 * 三个线程依次打印ABC, 打印n次， 线程1：A, 线程2：B, 线程3：C, 线程1：A...
 *
 * @author Williami
 * @description
 * @date 2022/1/17
 */
public class Wait_Notify_12 {

    private static int count;

    private static final Object lock = new Object();

    public static void main(String[] args) throws Exception {
        // *************************打印lock对象内存布局************************
        System.out.println(ClassLayout.parseInstance(lock).toPrintable());
        // *************************打印lock对象内存布局************************

        Thread thread1 = new Thread(() -> {
            printf(0);
        }, "A");

        Thread thread2 = new Thread(() -> {
            printf(1);

        }, "B");

        Thread thread3 = new Thread(() -> {
            printf(2);
        }, "C");

        thread1.start();
        thread2.start();
        thread3.start();

    }

    private static void printf(int targetNum) {
        IntStream.rangeClosed(1, 2).forEach(i -> {
            synchronized (lock) {
                System.out.println(Thread.currentThread().getName()+"   "+ClassLayout.parseInstance(lock).toPrintable());
                while (count % 3 != targetNum) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log(count);
                count++;
                // 给等待lock对象的monitor所有线程通知，_WaitSet -> _EntryList
                // 执行monitorexit释放monitor对象
                lock.notifyAll();
            }
        });
    }


    private static void log(int message) {
        System.out.println(Thread.currentThread().getName() + " " + message);
        System.out.println("----");
    }
}
