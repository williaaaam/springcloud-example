package com.example.springcloud.rocketmq.test.wait_notify;

/**
 * 顺序执行三个线程
 *
 * @author Williami
 * @description
 * @date 2022/1/17
 */
public class Wait_Notify_1 {

    private static int count;

    private static final Object lock = new Object();

    public static void main(String[] args) throws Exception {

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

        thread1.join();
        thread2.join();
        thread3.join();

    }

    private static void printf(int targetNum) {
        try {
            synchronized (lock) {
                while (count % 3 != targetNum) {
                    lock.wait();
                }
                log(targetNum);
                count++;
                // 给等待lock对象的monitor所有线程通知，_WaitSet -> _EntryList
                // 执行monitorexit释放monitor对象
                lock.notifyAll();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private static void log(int message) {
        System.out.println(Thread.currentThread().getName() + " " + message);
    }
}
