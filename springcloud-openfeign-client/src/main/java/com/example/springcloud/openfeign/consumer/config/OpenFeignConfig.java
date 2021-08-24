package com.example.springcloud.openfeign.consumer.config;

import com.example.springcloud.openfeign.consumer.service.GitHubService;
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
    GitHubService gitHubService2() {
        return null;
    }

}
