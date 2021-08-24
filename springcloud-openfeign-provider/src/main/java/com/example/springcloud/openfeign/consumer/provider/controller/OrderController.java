package com.example.springcloud.openfeign.consumer.provider.controller;

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

    @GetMapping("order/get")
    public String getOrder() {
        return "Order[id=1,signNo=20210824]";
    }

    @PostMapping("order/update")
    public boolean updateOrder() {
        return Boolean.TRUE;
    }
}
