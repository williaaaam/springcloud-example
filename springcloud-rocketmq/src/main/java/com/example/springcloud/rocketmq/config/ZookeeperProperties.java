package com.example.springcloud.rocketmq.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Williami
 * @description
 * @date 2021/9/27
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "zookeeper")
public class ZookeeperProperties {

    /**
     * 重试次数
     */
    private int retryCount;

    /**
     * 重试间隔时间
     */
    private int elapsedTimeInMs;

    /**
     * zk server连接地址,多个地址之间使用','隔开
     */
    private String address;

    /**
     * Session过期时间
     */
    private int sessionTimeoutInMs;

    /**
     * 连接超时时间
     */
    private int connectionTimeoutInMs;

}
