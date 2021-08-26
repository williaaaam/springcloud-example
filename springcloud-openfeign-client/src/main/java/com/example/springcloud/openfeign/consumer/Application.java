package com.example.springcloud.openfeign.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

//@EnableEurekaClient 不启动该注解，classpath下有spring-cloud-starter-netflix-eureka-client，也会自动注册自己到Eureka Server
// 开启Feign客户端,并制定扫描的包
@EnableFeignClients("com.example.springcloud.openfeign.consumer.service")
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

}
