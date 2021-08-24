package com.example.springcloud.openfeign.consumer.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Williami
 * @description
 * @date 2021/8/24
 */
@FeignClient(name = "springcloud-openfeign-provider")
public interface OrderService {

    @GetMapping("api/order/get")
    public String get();

    @PostMapping("api/order/update")
    public boolean update();
}
