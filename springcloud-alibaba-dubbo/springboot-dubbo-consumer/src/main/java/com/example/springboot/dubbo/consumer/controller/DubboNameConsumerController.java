package com.example.springboot.dubbo.consumer.controller;

import com.example.dubbo.interfaces.IName;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Williami
 * @description
 * @date 2021/12/4
 */
@RestController
@RequestMapping("/dubbo")
public class DubboNameConsumerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DubboNameConsumerController.class);
    /**
     * 默认协议 dubbo，默认端口20880
     * 1. 服务启动时订阅注册中心服务地址列表
     * 2. 生成IName提供者服务的代理对象
     * 3. url:服务消费者通过url直连服务提供者,这时注册中心失效,点对点连接
     */
    // <dubbo:reference>
    @DubboReference(version = "v1", url = "dubbo://127.0.0.1:20883//com.example.dubbo.interfaces.IName")
    private IName iName; // org.apache.dubbo.common.bytecode.proxy0@606238ce

    @DubboReference(version = "timeout",timeout = 10000)
    private IName timeout;

    @GetMapping("/user/name")
    public String getName(@RequestParam("name") String userName) {
        LOGGER.info(">>> Reference IName = {}", iName);
        return iName.getName(userName);
    }

    @GetMapping("timeout")
    public String getTimeoutName() {
        return timeout.getName("TimeOut");
    }


}
