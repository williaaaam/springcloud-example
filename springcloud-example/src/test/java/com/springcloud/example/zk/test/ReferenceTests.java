package com.springcloud.example.zk.test;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * @author Williami
 * @description
 * @date 2022/2/23
 */
public class ReferenceTests<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReferenceTests.class);

    @Test
    public void testPhantomReference() {
        Object o = new Object();
        ReferenceQueue<Object> refQue = new ReferenceQueue<>();
        PhantomReference<Object> phantomReference = new PhantomReference<>(o, refQue);
        // 显示将强引用赋值为null
        o = null;
        // Object对象当前幻象可达
        Runtime.getRuntime().gc();
        try {
            // remove是阻塞方法或者一直阻塞
            // PECS
            Reference<? extends Object> reference = refQue.remove(1000L);
            if (reference != null) {
                // 除了幻象引用（因为 get 永远返回 null），如果对象还没有被销毁，都可以通过 get 方法获取原有对象。这意味着，利用软引用和弱引用，我们可以将访问到的对象，重新指向强引用，也就是人为的改变了对象的可达性状态！
                // 消费
                System.out.println(reference.get()); // null
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static private class Lock {
    }

    private static Lock lock = new Lock();

    /**
     * @param timeout mills
     * @return
     * @throws IllegalArgumentException
     * @throws InterruptedException
     */
    public Reference<? extends T> remove(long timeout) throws IllegalArgumentException, InterruptedException {
        if (timeout < 0) {
            throw new IllegalArgumentException("Negative timeout value");
        }
        synchronized (lock) {
            Reference<? extends T> r = reallyPoll();
            if (r != null) return r;
            long start = (timeout == 0) ? 0 : System.nanoTime();
            for (; ; ) {
                LOGGER.info("准备休眠{}ms", timeout);
                lock.wait(timeout);
                LOGGER.info("结束休眠{}ms", timeout);
                r = reallyPoll();
                if (r != null) return r;
                if (timeout != 0) {
                    long end = System.nanoTime();
                    // 语法糖 1000_000
                    timeout -= (end - start) / 1000_000;
                    LOGGER.info("end={},start={},end-start={}ms,timeout = {}ms", end / 1000_000, start / 1000_000, (end - start) / 1000_000, timeout);
                    if (timeout <= 0) return null;
                    start = end;
                }
            }
        }
    }

    Reference<? extends T> reallyPoll() {
        return null;
    }

    @Test
    public void testRemove() throws InterruptedException {
        remove(2000);
    }

    public static void testSoftReference() {
        ReferenceQueue<byte[]> referenceQueue = new ReferenceQueue<>();
        SoftReference<byte[]> softReference = new SoftReference<>(new byte[1024 * 1024 * 8], referenceQueue);
        List<byte[]> list = new ArrayList<>();
        while (true) {
            list.add(new byte[1024 * 1024]);
            if (softReference.get() != null) {
                System.out.println("未被回收");
            }
            // poll轮询引用队列
            if (referenceQueue.poll() instanceof SoftReference) {
                System.out.println("开始回收byte[]");
            }

        }
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadLocal<byte[]> threadLocal = new ThreadLocal<byte[]>() {
            @Override
            protected byte[] initialValue() {
                // 10m内存
                return new byte[5 * 1024 * 1024];
            }
        };

        TimeUnit.SECONDS.sleep(30L);

        ExecutorService executor = new ThreadPoolExecutor(10, 20, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), new ThreadFactoryBuilder().setNameFormat("OhMyThread-%d").build(), new ThreadPoolExecutor.AbortPolicy());
        IntStream.rangeClosed(0, 30).forEach(i -> {
            executor.execute(() -> {
                System.out.println(Thread.currentThread().getName() + " " + threadLocal.get().length);
                // 模拟ThreadLocal内存泄漏
                //threadLocal.remove();
            });
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        executor.shutdown();
    }

    /**
     * 模拟ThreadLocal内存泄漏
     */
    public static void mockThreadLocalMemoryLeak() {

    }

}
