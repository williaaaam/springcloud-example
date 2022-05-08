package com.example.dubbo.interfaces.provider.impl;

import com.example.dubbo.interfaces.provider.IName;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.rpc.AsyncContext;
import org.apache.dubbo.rpc.RpcContext;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author Williami
 * @description
 * @date 2021/12/4
 */
@Slf4j
public class DubboName implements IName {

    @Override
    public String setName() {
        return "Provider_Dubbo#setName";
    }

    @Override
    public String getName(String param) {
        return "Provider_Dubbo#" + param;
    }

    @Override
    public String getNameAsync(Integer userId) {
        AsyncContext asyncContext = RpcContext.startAsync();
        new Thread(() -> {
            // 如果要使用上下文，则必须要放在第一句执行
            asyncContext.signalContextSwitch();
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            asyncContext.write("Hello, Russia");
        }).start();
        return null;
    }

    @Override
    public CompletableFuture<String> getNameFuture() {
        return new CompletableFuture<>().supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(5L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Hi,Russia !";
        });
    }

}
