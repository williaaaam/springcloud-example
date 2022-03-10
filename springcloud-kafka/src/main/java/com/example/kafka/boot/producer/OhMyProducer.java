package com.example.kafka.boot.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Williami
 * @description
 * @date 2022/3/10
 */
@RestController
@RequestMapping("msg")
public class OhMyProducer {

    private static final String TOPIC_NAME = "my-replicated-topic";

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    @RequestMapping("send")
    public String senndMessage() {
        kafkaTemplate.send(TOPIC_NAME, 0, "key", "message");
        return "send success";
    }

}
