package com.example.springcloud.demo.docker.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.Lifecycle;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Williami
 * @description
 * @date 2021/12/9
 */
@Component
public class DemoEventHandler implements Lifecycle {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemoListener.class);

    private AtomicBoolean running = new AtomicBoolean(false);

    /**
     * Spring容器在启动过程中通过EventListenerMethodProcessor处理器完成对 @EventListener注解的处理，并生成ApplicationListener注册到容器中
     *
     * @param event
     */
    @EventListener
    public void onApplicationEvent(DemoEvent event) {
        LOGGER.info(">>> {} DemoEventHandler#onApplicationEvent, 触发DemoEvent", Thread.currentThread().getName());
        event.fire();
    }

    @Override
    public void start() {
        LOGGER.info(">>> {} DemoEventHandler start()", Thread.currentThread().getName());
        this.running.set(true);
    }

    @Override
    public void stop() {
        LOGGER.info(">>> {} DemoEventHandler stop()", Thread.currentThread().getName()); // print >>> SpringContextShutdownHook DemoEventHandler stop()
        this.running.set(false);
    }

    @Override
    public boolean isRunning() {
        return this.running.get();
    }

}
