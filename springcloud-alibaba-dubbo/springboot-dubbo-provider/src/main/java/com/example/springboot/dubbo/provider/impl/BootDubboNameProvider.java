package com.example.springboot.dubbo.provider.impl;

import com.example.dubbo.interfaces.IName;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.protocol.rest.support.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
@DubboService(version = "v1",timeout = 4000) // timeout 预期服务执行时间
@Path("dubbo") // REST服务必须要有@Path注解
public class BootDubboName implements IName {

    private static final Logger LOGGER = LoggerFactory.getLogger(BootDubboName.class);

    @GET
    @Path("name")
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_PLAIN_UTF_8})
    @Override
    public String getName(String param) {
        URL url = RpcContext.getContext().getUrl();
        LOGGER.info(">>> Dubbo RpcContext, url = {}", url);
        return "dubbo-spring-boot-starter#ProviderV2:rest " + param;
    }

}
