package com.springcloud.example.zk.config;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Williami
 * @description
 * @date 2021/9/27
 */
@Configuration
public class ZookeeperConfig {

    /**
     * 创建 CuratorFramework 对象并连接 Zookeeper
     *
     * @param zkProperties 从 Spring 容器载入 zkProperties Bean 对象，读取连接 ZK 的参数
     * @return CuratorFramework
     */
    @Bean(initMethod = "start")
    public CuratorFramework curatorFramework(ZookeeperProperties zkProperties) {
        return CuratorFrameworkFactory.newClient(
                zkProperties.getAddress(),
                zkProperties.getSessionTimeoutInMs(),
                zkProperties.getConnectionTimeoutInMs(),
                new RetryNTimes(zkProperties.getRetryCount(),
                        zkProperties.getElapsedTimeInMs()));
    }
}
