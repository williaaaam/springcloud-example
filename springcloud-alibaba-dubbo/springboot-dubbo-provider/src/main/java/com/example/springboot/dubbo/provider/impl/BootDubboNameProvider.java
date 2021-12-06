package com.example.springboot.dubbo.provider.impl;

import com.example.dubbo.interfaces.IName;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.protocol.rest.support.ContentType;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * 暴露服务IName,DubboName也会自动注入到IOC容器
 *
 * @author Williami
 * @description
 * @date 2021/12/4
 */
@DubboService(version = "v2")
@Path("/dubbo") // REST服务必须要有@Path注解
public class BootDubboNameProvider implements IName {

    @GET
    @Path("/name")
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_PLAIN_UTF_8})
    @Override
    public String getName(String param) {
        return "dubbo-spring-boot-starter#ProviderV2:rest " + param;
    }

}
