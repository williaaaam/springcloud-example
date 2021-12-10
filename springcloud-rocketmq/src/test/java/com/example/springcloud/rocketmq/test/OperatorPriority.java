package com.example.springcloud.rocketmq.test;

/**
 * @author Williami
 * @description
 * @date 2021/12/10
 */
public class OperatorPriority {

    // 运算符优先级
    public static void main(String[] args) {

        int i = 0;
        // 5
        int j = ++i * 5;
        // m = 25, j = 6
        int m = j++ * 5;
        // 5,1,25
        System.out.println(j + " " + i + " " + m);

    }
}
