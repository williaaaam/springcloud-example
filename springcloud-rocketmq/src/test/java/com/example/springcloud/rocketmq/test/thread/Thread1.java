package com.example.springcloud.rocketmq.test.thread;

public class Thread1 implements Runnable {

    // 共享资源(临界资源)
    static int i = 0;

    @Override
    public synchronized void run() {
        for (int j = 0; j < 10000; j++) {
            // 获得了String的类锁
            // monitorenter ...
            i++;
            // monitorexit ...
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread1 t = new Thread1();
        Thread t1 = new Thread(t);
        Thread t2 = new Thread(t);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(i);
    }
    /**
     * 输出结果:
     * 20000
     */
}