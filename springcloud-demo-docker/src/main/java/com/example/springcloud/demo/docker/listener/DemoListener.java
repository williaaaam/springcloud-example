package com.example.springcloud.demo.docker.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;

/**
 * @author Williami
 * @description
 * @date 2021/12/9
 */
public class DemoListener implements ApplicationListener<DemoEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemoListener.class);


    @Override
    public void onApplicationEvent(DemoEvent event) {
        LOGGER.info("{} DemoListener#onApplicationEvent, 触发DemoEvent", Thread.currentThread().getName());
        event.fire();
    }

}
