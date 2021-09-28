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

        return CuratorFrameworkFactory
                .builder()
                // namespace不能添加'/'前缀，否则报异常Invalid namespace: /CuratorFramework, Invalid path string "//CuratorFramework" caused by empty node name specified @1 Spring容器启动失败
                // 如果/CuratorFramework不存在任何子节点后，zk server会自动删除该namespace对应的节点？
                .namespace("Ticket12306")
                .connectString(zkProperties.getAddress())
                .sessionTimeoutMs(zkProperties.getSessionTimeoutInMs())
                .connectionTimeoutMs(zkProperties.getConnectionTimeoutInMs())
                .retryPolicy(new RetryNTimes(zkProperties.getRetryCount(),
                        zkProperties.getElapsedTimeInMs()))
                .build();

        /**
         * Create a new client
         *
         * @param connectString       list of servers to connect to; zk server地址和端口，","分隔
         * @param sessionTimeoutMs    session timeout
         * @param connectionTimeoutMs connection timeout
         * @param retryPolicy         retry policy to use
         * @return client
         */
        /*return CuratorFrameworkFactory.newClient(
                zkProperties.getAddress(),
                zkProperties.getSessionTimeoutInMs(),
                zkProperties.getConnectionTimeoutInMs(),
                new RetryNTimes(zkProperties.getRetryCount(),
                        zkProperties.getElapsedTimeInMs()));*/
    }
}
