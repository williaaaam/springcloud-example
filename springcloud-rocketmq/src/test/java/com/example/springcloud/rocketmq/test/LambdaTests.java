package com.example.springcloud.rocketmq.test;

import java.util.stream.IntStream;

/**
 * @author Williami
 * @description
 * @date 2021/11/12
 */
public class LambdaTests {

    private int i = 10;

    public static void main(String[] args) {
        int x = 1;
        //IntStream.of(1, 2, 3).map(i -> i * 2).map(i -> i * x);
        IntStream.of(1, 2, 3).map(i -> i * 2);
        // 方法引用不会生成额外的方法
        IntStream.of(1, 2, 3).map(Integer::hashCode);
    }
}
