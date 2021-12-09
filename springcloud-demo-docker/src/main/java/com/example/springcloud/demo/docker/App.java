package com.example.springcloud.demo.docker;

import com.example.springcloud.demo.docker.listener.DemoEvent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class App {

    public static void main(String[] args) {
        // 自动执行registerShutdownHook()方法，注册名为SpringShutdownHook的线程到JVM Runtime
        ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(App.class, args);
        configurableApplicationContext.start();

        // 1. 注册监听器
        //configurableApplicationContext.addApplicationListener(new DemoListener());

        // 2. 发布事件
        configurableApplicationContext.publishEvent(new DemoEvent(new Object(), "发布事件"));
    }

}
