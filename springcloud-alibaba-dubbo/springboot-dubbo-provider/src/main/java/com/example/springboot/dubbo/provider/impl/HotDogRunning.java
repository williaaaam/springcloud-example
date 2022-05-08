package com.example.springboot.dubbo.provider.impl;

import com.example.springboot.dubbo.provider.spi.IRun;

/**
 * @author Williami
 * @description
 * @date 2021/12/6
 */
public class HotDogRunning implements IRun {

    static {
        System.out.println("HotDogRunning加载了");
    }

    public HotDogRunning() {
        System.out.println("Dog实例化");
    }

}
