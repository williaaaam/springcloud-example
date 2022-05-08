package com.example.springcloud.rocketmq.test.thread;

import java.util.concurrent.TimeUnit;

/**
 * @author Williami
 * @description
 * @date 2022/1/17
 */
public class SyncThread implements Runnable {

    private static int count;

    private static String lock = "lock";

    public SyncThread() {
        count = 0;
    }

    public void run() {
        synchronized (lock) {
            System.out.println(Thread.currentThread().getName() + "enter ");
            try {
                // 等待十秒
                lock.wait(10 * 1000);
                System.out.println(Thread.currentThread().getName() + " end");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void wakeUp() throws InterruptedException {
        System.out.println("wakeUp "+Thread.currentThread().getName());
        TimeUnit.SECONDS.sleep(4);
        synchronized (lock){
            // notify也能唤醒wait(long timeout)
            // notify()并不会立刻唤醒等待线程，还需要等待下面的代码依次执行完成，在这期间主线程依然还持有lock对象的monitor
            lock.notify();
            System.out.println(Thread.currentThread().getName()+"休眠2s");
            TimeUnit.SECONDS.sleep(2);
            // 此行通过debug也可以发现，run（）方法不会执行到26行
            System.out.println(Thread.currentThread().getName()+"休眠2s结束，开始唤醒线程SyncThread1");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SyncThread syncThread = new SyncThread();
        Thread thread1 = new Thread(syncThread, "SyncThread1");
        //Thread thread2 = new Thread(syncThread, "SyncThread2");
        thread1.start();
        //thread2.start();
        syncThread.wakeUp();
    }
}
