package com.example.springcloud.openfeign.consumer.config;

import com.example.springcloud.openfeign.consumer.service.GitHubClient;
import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author Williami
 * @description
 * @date 2021/8/20
 */
@Configuration(proxyBeanMethods = false)
public class OpenFeignConfig {


    @Bean(autowireCandidate = false)
    @Primary
    GitHubClient gitHubService2() {
        return null;
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        /**
         * NONE：不记录任何日志信息，这是默认值。
         *
         * BASIC：仅记录请求的方法，URL以及响应状态码和执行时间
         *
         * HEADERS：在BASIC的基础上，额外记录了请求和响应的头信息
         *
         * FULL：记录所有请求和响应的明细，包括头信息、请求体、元数据
         */
        return Logger.Level.FULL;
    }

}
