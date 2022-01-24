package com.example.springcloud.rocketmq.test.juc;

import java.util.concurrent.TimeUnit;

/**
 * @author Williami
 * @description
 * @date 2022/1/21
 */
public class SynchronizedTests {

    private static Object lock = new Object();


    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            synchronized (lock) {
                try {
                    System.out.println("Thread1获得锁");
                    //TimeUnit.SECONDS.sleep(3600L);
                    while (true){
                        // wait sleep均可响应中断
                        lock.wait();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    System.out.println("Thread1释放锁");
                }
            }
        });


        Thread thread2 = new Thread(() -> {
            synchronized (lock) {
                System.out.println("Thread2获得锁");
                System.out.println("Thread2释放锁");
            }
        });


        thread1.start();

        System.out.println("2s后将开始中断线程1");
        TimeUnit.MILLISECONDS.sleep(2000L);

        thread2.start();

        System.out.println("开始中断Thread1");
        // 中断线程1
        thread1.interrupt();

    }
}
