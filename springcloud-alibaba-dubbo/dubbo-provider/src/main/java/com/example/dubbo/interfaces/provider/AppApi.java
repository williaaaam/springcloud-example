package com.example.dubbo.interfaces.provider;

import com.example.dubbo.interfaces.provider.impl.DubboName;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.ServiceConfig;

/**
 * @author Williami
 * @description
 * @date 2022/3/3
 */
public class AppApi {

    /**
     * API方式启动服务提供方，仅用于测试；官方推荐使用XML
     *
     * @param args
     */
    public static void main(String[] args) {
        // 服务实现类
        IName nameService = new DubboName();

        // 当前应用配置
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("dubbo-provider");
        applicationConfig.setOwner("Sihai");

        // 注册中心配置
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress("ip:2180,ip:2181,ip:2182");
        registryConfig.setGroup("zk_dubbo");
        registryConfig.setVersion("*");
        registryConfig.setProtocol("zookeeper");
        registryConfig.setId("zk_registry");

        // 协议配置
        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setName("dubbo");
        protocolConfig.setPort(20880);
        protocolConfig.setThreads(200);
        protocolConfig.setThreadname("OhMyThread-%d");


        // 服务提供者配置
        // 注意：ServiceConf
        // 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏ig为重对象，内部封装了与注册中心的连接，以及开启服务端口
        ServiceConfig<IName> serviceConfig = new ServiceConfig<>();
        serviceConfig.setApplication(applicationConfig);
        serviceConfig.setRegistry(registryConfig);
        serviceConfig.setProtocol(protocolConfig);
        serviceConfig.setInterface(IName.class);
        serviceConfig.setRef(nameService);
        serviceConfig.setVersion("*");

        // 暴露以及注册服务
        serviceConfig.export();
    }
}
