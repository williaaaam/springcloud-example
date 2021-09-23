package com.example.springcloud.tranction.test;

import com.example.springcloud.transaction.aspect.OhMyAspect;
import com.example.springcloud.transaction.config.AppConfig;
import com.example.springcloud.transaction.service.FooService;
import com.example.springcloud.transaction.service.OhMyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Williami
 * @description
 * @date 2021/9/18
 */
public class TransactionTests {

    @Test
    public void transaction() {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        OhMyAspect bean = annotationConfigApplicationContext.getBean(OhMyAspect.class);
        //System.out.println(bean);
        OhMyService ohMyService = annotationConfigApplicationContext.getBean(OhMyService.class);
        ohMyService.update();
    }

    @DisplayName("测试事务传播行为")
    @Test
    public void nestedTransaction() {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        FooService fooService = annotationConfigApplicationContext.getBean(FooService.class);
        System.out.println("bool返回 = " + fooService.bool());
    }

}
