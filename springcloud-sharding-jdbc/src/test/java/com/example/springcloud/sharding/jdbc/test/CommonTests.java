package com.example.springcloud.sharding.jdbc.test;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Williami
 * @description
 * @date 2021/9/26
 */
public class CommonTests {

    @Test
    public void intRange() {
        List<Integer> integers = IntStream.range(1, 11).boxed().collect(Collectors.toList());
        integers.parallelStream().forEach(i -> {
            System.out.println(i + " " + Thread.currentThread().getName());
        });
    }
}
