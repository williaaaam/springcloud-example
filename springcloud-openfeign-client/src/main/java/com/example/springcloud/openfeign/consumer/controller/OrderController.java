package com.example.springcloud.openfeign.consumer.controller;

import com.example.springcloud.openfeign.consumer.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Williami
 * @description
 * @date 2021/8/24
 */
@RestController
@RequestMapping("api")
public class OrderController {


    @Autowired
    private OrderService orderService;

    @GetMapping("order/get")
    public String getOrder() {
        return orderService.get();
    }


    @PostMapping("order/update")
    public boolean updateOrder() {
        return orderService.update();
    }

}
