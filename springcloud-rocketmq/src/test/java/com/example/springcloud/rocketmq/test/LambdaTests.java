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
    }

    private long foo() {
        return IntStream.range(1, 10).map(i -> i * 2).map(j -> bar(i + j)).count();
    }

    private int bar(int j) {
        return j + 1;
    }
}
