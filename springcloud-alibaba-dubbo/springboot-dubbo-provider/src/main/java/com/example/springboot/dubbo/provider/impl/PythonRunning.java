package com.example.springboot.dubbo.provider.impl;

import com.example.springboot.dubbo.provider.spi.IRun;

/**
 * @author Williami
 * @description
 * @date 2021/12/6
 */
public class PythonRunning implements IRun {

    static {
        System.out.println("PythonRunning加载了");
    }

    public PythonRunning() {
        System.out.println("Python实例化");
    }

}
