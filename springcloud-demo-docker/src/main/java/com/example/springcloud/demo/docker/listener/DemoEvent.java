package com.example.springcloud.demo.docker.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;

/**
 * @author Williami
 * @description
 * @date 2021/12/9
 */
public class DemoEvent extends ApplicationEvent {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemoEvent.class);

    private String text;


    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null}) 事件源
     */
    public DemoEvent(Object source) {
        super(source);
    }

    public DemoEvent(Object source, String text) {
        super(source);
        this.text = text;
    }

    public void fire() {
        LOGGER.info(">>> " + Thread.currentThread().getName() + "DemoEvent#fire" + this.text);
    }

}
