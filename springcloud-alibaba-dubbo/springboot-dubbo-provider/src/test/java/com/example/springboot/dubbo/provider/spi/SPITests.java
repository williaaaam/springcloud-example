package com.example.springboot.dubbo.provider.spi;

import org.apache.dubbo.common.extension.ExtensionLoader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ServiceLoader;

/**
 * @author Williami
 * @description
 * @date 2021/12/6
 */
public class SPITests {

    /**
     *
     */
    @DisplayName("测试jdk自带SPI机制")
    @Test
    public void testWithJDKSPI() {
        // META-INF/services/clazz.name(大小写不敏感)
        ServiceLoader<IRun> serviceLoader = ServiceLoader.load(IRun.class);
        for (IRun category : serviceLoader) {
            System.out.println(category.getClass().getName());
        }
    }

    /**
     * Dubbo SPI机制和JDK spi一样，都要求构造器修饰符是public（反射）,jdk会实例化所有实现类，而Dubbo会加载所有的实现类，并仅实例化指定实现类
     */
    @DisplayName("测试Dubbo扩展SPI机制")
    @Test
    public void testWithDubboSPI() {
        // META-INF/services/clazz.name(大小写不敏感)
        ExtensionLoader<IRun> serviceLoader = ExtensionLoader.getExtensionLoader(IRun.class);
        System.out.println(serviceLoader.getExtension("python"));
    }

}
