package com.example.springcloud.openfeign.consumer.service;

import org.springframework.stereotype.Component;

/**
 * The fallback class must
 * implement the interface annotated by this annotation and be a valid spring bean.
 *
 * @author Williami
 * @description
 * @date 2021/8/24
 */
@Component
public class OrderFallback implements OrderClient {

    @Override
    public String get() {
        return "服务器繁忙,稍后再试";
    }

    @Override
    public boolean update() {
        return false;
    }

}
