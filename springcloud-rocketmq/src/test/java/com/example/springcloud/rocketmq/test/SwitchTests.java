package com.example.springcloud.rocketmq.test;

/**
 * @author Williami
 * @description
 * @date 2021/11/22
 */
public class SwitchTests {

    public static void main(String[] args) {
        Identifier identifier = Identifier.A;
        int i = 0;
        switch (identifier) {
            case A:
                i++;
            case B:
                i++;
        }

        System.out.println(i);
    }

    enum Identifier {
        A, B
    }
}
