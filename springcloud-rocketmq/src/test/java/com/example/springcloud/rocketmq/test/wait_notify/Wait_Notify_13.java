package com.example.springcloud.rocketmq.test.wait_notify;

/**
 * 两个线程交替打印1-20的奇偶数
 *
 * @author Williami
 * @description
 * @date 2022/1/17
 */
public class Wait_Notify_13 {

    //private static int count = 1;
    private static int count = 0;

    private static final Object lock = new Object();

    public static void main(String[] args) throws Exception {

        Thread thread1 = new Thread(() -> {
            printf(0);
        }, "A");

        Thread thread2 = new Thread(() -> {
            printf(1);

        }, "B");

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

    }

    private static void printfv2(int targetNum) {
        synchronized (lock) {
            while (count < 20) {
                while (count % 2 != targetNum) {
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
        }
    }

    private static void printf(int targetNum) {
        synchronized (lock) {
            while (count < 10) {
                try {
                    System.out.print(Thread.currentThread().getName() + "：");
                    System.out.println(++count);
                    lock.notifyAll();
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 防止count=10后，while()循环不再执行，有子线程被阻塞未被唤醒，导致主线程不能退出
            lock.notifyAll();
        }
    }


    private static void log(int message) {
        System.out.println(Thread.currentThread().getName() + " " + message);
        if (message % 2 == 0) {
            System.out.println("----");
        }
    }

    private static void log(String message) {
        System.out.println(Thread.currentThread().getName() + " " + message);
        System.out.println("----");
    }
}
