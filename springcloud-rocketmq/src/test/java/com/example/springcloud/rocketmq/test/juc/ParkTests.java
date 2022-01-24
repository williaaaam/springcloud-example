package com.example.springcloud.rocketmq.test.juc;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @author Williami
 * @description
 * @date 2022/1/20
 */
public class ParkTests {

    public static void main(String[] args) throws InterruptedException {


        Thread thread1 = new Thread(() -> {
            System.out.println("挂起当前线程");
            // 消耗一个许可，阻塞当前线程
            LockSupport.park();
            System.out.println("Thread1被唤醒，继续执行");
        });

        thread1.start();

        //System.out.println("10 s后唤醒线程Thread1");
        System.out.println("10 s后中断线程Thread1");
        TimeUnit.SECONDS.sleep(10L);

        //LockSupport.unpark(thread1);
        // 中断Thread1
        thread1.interrupt();

    }
}
