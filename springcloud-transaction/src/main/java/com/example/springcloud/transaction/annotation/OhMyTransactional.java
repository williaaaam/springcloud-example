package com.example.springcloud.transaction.annotation;

import java.lang.annotation.*;

/**
 * @author Williami
 * @description
 * @date 2021/9/16
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Inherited
public @interface OhMyTransactional {
}
