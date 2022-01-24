package com.example.springcloud.rocketmq.test.juc;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Williami
 * @description
 * @date 2022/1/24
 */
public class ReentrantReadWriteLockTests {

    public static void main(String[] args) {
        ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock(true);
        ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();
        ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();

        // ReentrantReadWriteLock不支持锁升级,但支持锁降级
        System.out.println("准备获取读锁");
        readLock.lock();
        System.out.println("开始升级为写锁");
        // 线程WAITING(parking)
        writeLock.lock();
        System.out.println("持有写锁");
        System.out.println("开始释放写锁");
        writeLock.unlock();
        System.out.println("写锁已经释放，开始释放读锁");
        readLock.unlock();
        System.out.println("读锁已经释放");
    }


}
