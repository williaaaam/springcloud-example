package com.example.dubbo.interfaces.consumer;

import com.example.dubbo.interfaces.provider.IName;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * @author Williami
 * @description
 * @date 2021/12/4
 */
public class App {

    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * consumer://10.241.25.214/com.example.dubbo.interfaces.provider.IName?application=dubbo-consumer&category=consumers&check=false&dubbo=2.0.2&init=false&interface=com.example.dubbo.interfaces.provider.IName&metadata-type=remote&methods=setName,getName&pid=16612&release=2.7.8&side=consumer&sticky=false&timestamp=1638594788584
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"dubbo-consumer.xml"});
        context.start();
        // 获取远程服务代理
        IName nameService = (IName) context.getBean("nameService");
        // 执行远程方法
        //String hello = nameService.getName();
        // 显示调用结果
        LOGGER.info(">>> invoke rpc and then response value = {}", () -> nameService.getName("Ping"));
        // 当选用dubbo协议的时候， nameService = org.apache.dubbo.common.bytecode.proxy0@47c40b56
        LOGGER.info(">>> nameService = {}", nameService);
        System.in.read();
    }
}
