package com.example.springcloud.rocketmq.test.juc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 借助ReentrantLock实现有界阻塞队列
 */
class BoundedBuffer {

    final Lock lock = new ReentrantLock();

    // condition 实例依赖于 lock 实例
    final Condition notFull = lock.newCondition();
    final Condition notEmpty = lock.newCondition();

    final Object[] items = new Object[1];

    int putPtr, takePtr, count;

    public void put(Object x) throws InterruptedException {
        lock.lock();
        try {
            //  put 时判断是否已经满了
            // 则线程在 notFull 条件上排队阻塞
            while (count == items.length) {
                // await和signal必须用在 lock...unlock之间, 否则会抛IllegalMonitorStateException
                notFull.await();
            }
            items[putPtr] = x;
            if (++putPtr == items.length) {
                putPtr = 0;
            }
            ++count;
            // put 成功之后，队列中有元素
            // 唤醒在 notEmpty 条件上排队阻塞的线程
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public Object take() throws InterruptedException {
        lock.lock();
        try {
            // take 时，发现为空
            // 则线程在 notEmpty 的条件上排队阻塞
            while (count == 0) {
                notEmpty.await();
            }
            Object x = items[takePtr];
            if (++takePtr == items.length) {
                takePtr = 0;
            }
            --count;
            // take 成功，队列不可能是满的
            // 唤醒在 notFull 条件上排队阻塞的线程
            notFull.signal();
            return x;
        } finally {
            lock.unlock();
        }
    }


    public static void main(String[] args) throws InterruptedException {
        BoundedBuffer boundedBuffer = new BoundedBuffer();
        boundedBuffer.put("11111");
        boundedBuffer.put("22222");
        boundedBuffer.put("33333");


        System.out.println(boundedBuffer.take());
        System.out.println(boundedBuffer.take());
        System.out.println(boundedBuffer.take());
    }


}