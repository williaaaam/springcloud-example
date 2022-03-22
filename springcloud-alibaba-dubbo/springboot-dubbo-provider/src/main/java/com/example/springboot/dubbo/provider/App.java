package com.example.springboot.dubbo.provider;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableDubbo
@SpringBootApplication
public class App {

    /**
     * dubbo://10.241.25.214:20882/com.example.dubbo.interfaces.IName?anyhost=true&application=springboot-dubbo-provider&deprecated=false&dubbo=2.0.2&dynamic=true&generic=false&interface=com.example.dubbo.interfaces.IName&metadata-type=remote&methods=getName&pid=4140&release=2.7.8&side=provider&timestamp=1638608506450
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}
