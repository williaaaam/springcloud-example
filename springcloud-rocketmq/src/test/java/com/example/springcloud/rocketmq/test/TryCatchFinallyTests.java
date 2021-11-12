package com.example.springcloud.rocketmq.test;

import org.junit.jupiter.api.Test;

/**
 * @author Williami
 * @description
 * @date 2021/11/11
 */
public class TryCatchFinallyTests {

    /**
     * 异常没有被捕获，finally代码块会直接运行。运行之后重新抛出该异常
     */
    public void doTryFinallyWithException() {
        try {
            int i = 1 / 0;
        } finally {
            System.out.println("2333333");
        }
    }


    @Test
    public void testTryFinallyWithException() {
        doTryFinallyWithException();
    }
}
