package com.example.dubbo.interfaces.provider.impl;

import com.example.dubbo.interfaces.provider.IName;
import lombok.extern.slf4j.Slf4j;

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
}
