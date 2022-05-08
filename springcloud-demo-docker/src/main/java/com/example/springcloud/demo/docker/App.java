package com.example.springcloud.demo.docker;

import com.example.springcloud.demo.docker.listener.DemoListener;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class App {

    public static void main(String[] args) {
        // 自动执行registerShutdownHook()方法，注册名为SpringShutdownHook的线程到JVM Runtime
        //ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(App.class, args);
        //configurableApplicationContext.start();
        // 1. 注册监听器
        DemoListener demoListener = new DemoListener();
        //configurableApplicationContext.addApplicationListener(demoListener);

        // cause NoSuchBeanDefinitionException
        //System.out.println(configurableApplicationContext.getBean(DemoListener.class));

        // 2. 发布事件
        //configurableApplicationContext.publishEvent(new DemoEvent(new Object(), "发布事件"));

        new SpringApplicationBuilder()
                .listeners(demoListener)
                .sources(App.class)
                .run(args);
    }

}