package com.example.springcloud.openfeign.consumer.service;

import com.example.springcloud.openfeign.consumer.config.OpenFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Williami
 * @description
 * @date 2021/8/24
 */
@FeignClient(name = "springcloud-openfeign-provider", fallback = OrderFallback.class, configuration = OpenFeignConfig.class)
public interface OrderClient {

    @GetMapping("api/order/get")
    public String get();

    @PostMapping("api/order/update")
    public boolean update();
}
