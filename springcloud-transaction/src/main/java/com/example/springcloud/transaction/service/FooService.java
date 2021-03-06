package com.example.springcloud.transaction.service;

import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author Williami
 * @description
 * @date 2021/9/22
 */
@Component
public class FooService {

    /*@Autowired
    FooService fooService;
*/
    @Transactional
    public Boolean bool() {
        //throw new RuntimeException("qwqwqw");
        /*System.out.println("bool 调用 dummy{}");
        dummy();
        return Boolean.TRUE;*/
        // 解决事务自调用的问题
        FooService currentProxy = (FooService)AopContext.currentProxy();
        currentProxy.dummy();
        return Boolean.TRUE;
    }

    /**
     * org.springframework.transaction.IllegalTransactionStateException: Existing transaction found for transaction marked with propagation 'never'
     * @return
     */
    @Transactional(propagation = Propagation.NEVER)
    public Boolean dummy() {
        System.out.println("dummy执行");
        return Boolean.TRUE;
    }

}
