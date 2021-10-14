package com.springcloud.example.zk.test;

import lombok.Data;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author Williami
 * @description
 * @date 2021/10/12
 */
public class JobTests {


    @DisplayName("延时任务")
    @Test
    public void delayedQueue() throws InterruptedException {
        List<String> orderList = Arrays.asList("00000001", "00000002", "00000003", "00000004", "00000005");
        // 该方案是利用JDK自带的DelayQueue来实现，这是一个无界阻塞队列，该队列只有在延迟期满的时候才能从中获取元素，放入DelayQueue中的对象，是必须实现Delayed接口的。
        DelayQueue<Bar> delayQueue = new DelayQueue<>();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 5; i++) {
            delayQueue.add(new Bar(orderList.get(i), TimeUnit.NANOSECONDS.convert(5L, TimeUnit.SECONDS)));
            // 每隔5s后取出 Bar
            delayQueue.take().run();
            System.out.println("After " +
                    (System.currentTimeMillis() - start) + " MilliSeconds");
        }
    }

    @Data
    static class Bar implements Delayed {

        private String orderId;

        private long timeoutInNanoTime;

        public Bar(String orderId, long timeoutInNanoTime) {
            this.orderId = orderId;
            this.timeoutInNanoTime = timeoutInNanoTime + System.nanoTime();
        }

        /**
         * 返回距离自定义的超时时间还有多少
         *
         * @param unit
         * @return
         */
        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(timeoutInNanoTime - System.nanoTime(), TimeUnit.NANOSECONDS);
        }

        /**
         * 按照超时时间，先过期的任务先拿出来
         *
         * @param o
         * @return
         */
        @Override
        public int compareTo(Delayed o) {
            if (o == this) {
                return 0;
            }

            Bar t = (Bar) o;

            long d = (getDelay(TimeUnit.NANOSECONDS) - t.getDelay(TimeUnit.NANOSECONDS));
            return (d == 0) ? 0 : ((d < 0) ? -1 : 1);
        }

        void run() {
            System.out.println(" 准备删除订单： " + orderId);
        }
    }

}
