package com.example.dubbo.interfaces.consumer;

import com.example.dubbo.interfaces.provider.IName;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
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

        LOGGER.info("----------------------------------------------------------------");
        LOGGER.info("Now invoke getNameFuture async");
        CompletableFuture<String> nameFuture = nameService.getNameFuture();

        long startTime = System.currentTimeMillis();
        long start = startTime;
        nameFuture.whenComplete((t, v) -> {
            long endTime = System.currentTimeMillis();
            long costs = (endTime - start) / 1000;
            LOGGER.info(">>> Server [getNameFuture] Response : {}, takes {} ms", t, costs);
        });

        nameService.getNameAsync(4);
        RpcContext rpcContext = RpcContext.getContext();
        CompletableFuture<Object> completableFuture = rpcContext.getCompletableFuture();
        startTime = System.currentTimeMillis();

        // 默认超时时间1s, 这里改成10s
        long _start = startTime;
        completableFuture.whenComplete((t, v) -> {
            long endTime = System.currentTimeMillis();
            long costs = (endTime - _start) / 1000;
            LOGGER.info(">>> Server [getNameAsync]Response : {}", t);
        });

        System.in.read();
    }
}
