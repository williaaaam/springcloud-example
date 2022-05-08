package com.example.dubbo.interfaces.provider;

import java.util.concurrent.CompletableFuture;

/**
 * @author Williami
 * @description
 * @date 2021/12/4
 */
public interface IName {

    /**
     *
     * @return
     */
    String setName();

    /**
     *
     * @return
     */
    String getName(String param);

    String getNameAsync(Integer userId);

    CompletableFuture<String> getNameFuture();
}
