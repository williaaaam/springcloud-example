package com.example.springcloud.rocketmq.controller;

import com.example.springcloud.rocketmq.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Williami
 * @description
 * @date 2021/10/13
 */
@Component
public class OrderController {

    @Autowired
    private static OrderService orderService;
}
