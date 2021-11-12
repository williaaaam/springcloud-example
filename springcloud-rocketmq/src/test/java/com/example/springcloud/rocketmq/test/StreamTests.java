package com.example.springcloud.rocketmq.test;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author Williami
 * @description
 * @date 2021/11/11
 */
public class StreamTests {

    @Test
    public void testStreamReturn() {
        List<String> list = Arrays.asList("2", "3", "4", "5", "6", "7");
        list.stream().forEach(s -> {
            if ("2".equalsIgnoreCase(s)) {
                return;
            }
            System.out.println(s);
        });

        System.out.println("============================================================================");
        list.stream().forEach(s -> {
            if ("5".equalsIgnoreCase(s)) {
                return;
            }
            System.out.println(s);
        });

        System.out.println("============================================================================");
        // invokeinterface
        // invokedynamic
        // invokeinterface
        list.stream().forEach(s -> {
            if ("6".equalsIgnoreCase(s)) {
                return;
            }
            System.out.println(s);
        });
    }
}
