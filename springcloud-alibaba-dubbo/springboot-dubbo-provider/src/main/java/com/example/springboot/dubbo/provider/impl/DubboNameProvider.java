package com.example.springboot.dubbo.provider.impl;

import com.example.dubbo.interfaces.IName;

/**
 * 暴露服务IName,DubboName也会自动注入到IOC容器
 *
 * @author Williami
 * @description
 * @date 2021/12/4
 */
//@DubboService(version = "v1", protocol = "protocol2")
public class DubboNameProvider implements IName {

    @Override
    public String getName(String param) {
        return "dubbo-spring-boot-starter#ProviderV1 " + param;
    }

}
