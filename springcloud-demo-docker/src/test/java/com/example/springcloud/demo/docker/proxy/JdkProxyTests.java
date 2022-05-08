package com.example.springcloud.demo.docker.proxy;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author Williami
 * @description
 * @date 2021/12/14
 */
public class JdkProxyTests {

    @Test
    public void testProxy() {
        // 保存生成的代理类
        System.setProperty("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        //System.setProperty("jdk.proxy.ProxyGenerator.saveGeneratedFiles", "true");
        // 目标对象
        Dog target = new Dog();
        Object obj1 = Proxy.newProxyInstance(Dog.class.getClassLoader(), IName.class.getInterfaces(), new DogHandler(target));
        System.out.println(obj1 instanceof Proxy);

        IName proxy = (IName) Proxy.newProxyInstance(Dog.class.getClassLoader(), Dog.class.getInterfaces(), new DogHandler(target));
        System.out.println(proxy instanceof Proxy);
        proxy.say("China");
    }
}

class DogHandler implements InvocationHandler {

    public DogHandler(Object target) {
        this.target = target;
    }

    // 被代理对象
    private Object target;

    /**
     * @param proxy  代理对象
     * @param method 目标方法
     * @param args   目标方法参数
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(target, args);
    }

}


class Dog implements IName {
    @Override
    public void say(String key) {
        System.out.println("Woh~" + key);
    }
}

interface IName {
    void say(String name);
}
