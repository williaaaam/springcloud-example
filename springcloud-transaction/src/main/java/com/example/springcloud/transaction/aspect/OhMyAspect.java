package com.example.springcloud.transaction.aspect;

import com.example.springcloud.transaction.utils.TransactionUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

/**
 * @author Williami
 * @description
 * @date 2021/9/18
 */
@Component
@Aspect
public class OhMyAspect {

    //@Pointcut("execution(public * com.example.springcloud.transaction.service..*.*(..)) && @annotation(com.example.springcloud.transaction.annotation.OhMyTransactional)")
    @Pointcut("@annotation(com.example.springcloud.transaction.annotation.OhMyTransactional)")
    private void pointcut() {
    }


    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws SQLException {
        // 方法执行前先开启事务
        TransactionUtil.start();

        // 执行业务逻辑
        Object proceed = null;
        try {
            proceed = joinPoint.proceed();
            System.out.println("方法返回值 = " + proceed);
            // 方法执行完成后提交事务
            TransactionUtil.commit();
        } catch (Throwable throwable) {
            // 出现异常进行回滚
            TransactionUtil.rollback();
            return proceed;
        }

        return proceed;
    }
}
