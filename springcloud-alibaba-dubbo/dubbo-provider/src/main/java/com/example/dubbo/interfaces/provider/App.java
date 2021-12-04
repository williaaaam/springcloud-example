package com.example.dubbo.interfaces.provider;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * @author Williami
 * @description
 * @date 2021/12/4
 */
public class App {

    /**
     * 应用程序启动后，会在/zk_dubbo/com.example.dubbo.interfaces.provider.IName/providers/节点下注册Provider的URL：dubbo://10.241.25.214:20880/com.example.dubbo.interfaces.provider.IName?anyhost=true&application=dubbo-provider&deprecated=false&dubbo=2.0.2&dynamic=true&generic=false&interface=com.example.dubbo.interfaces.provider.IName&metadata-type=remote&methods=setName,getName&pid=8472&release=2.7.8&side=provider&timestamp=16385930
     * <p></p>
     * 应用程序关闭后，即Provider下线后，会主动删除providers节点下相应的URL
     * <p>
     * 查看节点类型,get /zk_dubbo/com.example.dubbo.interfaces.provider.IName/providers 返回ephemeralOwner = 0x0表示持久节点，当ephemeralOwner不为0时，表示节点是临时节点，值为会话id
     * <p>
     * Provider下线后，节点会被主动删除，是临时节点？
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext(new String[]{"dubbo-provider.xml"});
        classPathXmlApplicationContext.start();
        // 按任意键退出
        System.in.read();
    }
}
