package com.example.springcloud.rocketmq.test;

/**
 * 虚拟机参数
 * -XX:+PrintGC
 * -XX:+PrintGCApplicationStoppedTime
 * -XX:+PrintSafepointStatistics
 * -XX:+UseCountedLoopSafepoints
 *
 * @author Williami
 * @description
 * @date 2021/11/15
 */
public class GCSafepointTests {

    static double sum = 0;

    public static void foo() {
        for (int i = 0; i < 0x77777777; i++) {
            sum += Math.sqrt(i);
        }
    }

    public static void bar() {
        for (int i = 0; i < 50_000_000; i++) {
            new Object().hashCode();
        }
    }

    public static void main(String[] args) {
        //new Thread(GCSafepointTests::foo).start();
        new Thread(GCSafepointTests::bar).start();
    }

}
