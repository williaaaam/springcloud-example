package com.example.springcloud.openfeign.consumer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.util.List;

/**
 * @author Williami
 * @description
 * @date 2021/8/26
 */
@SpringBootTest
public class EurekaClientTests {

    @Autowired
    private DiscoveryClient eurekaDiscoveryClient;

    @DisplayName("查询微服务实例清单")
    @Test
    public void fetchServices() {

        eurekaDiscoveryClient.getServices().stream().forEach(System.out::println);

        // 查询具体的微服务实例信息
        List<ServiceInstance> instances = eurekaDiscoveryClient.getInstances("SPRINGCLOUD-OPENFEIGN-PROVIDER");
        for (ServiceInstance serviceInstance : instances) {
            System.out.println(serviceInstance.getHost());
            System.out.println(serviceInstance.getPort());
            System.out.println(serviceInstance.getUri());
            System.out.println(serviceInstance.getServiceId());
            System.out.println(serviceInstance.getInstanceId());
        }

    }
}
