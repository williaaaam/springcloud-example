package com.example.springcloud.transaction.config;

import com.example.springcloud.transaction.aspect.OhMyAspect;
import com.example.springcloud.transaction.service.OrderService;
import com.example.springcloud.transaction.service.ProductionService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;

/**
 * @author Williami
 * @description
 * @date 2021/9/18
 */
@EnableAspectJAutoProxy
@Configuration
@ComponentScan(basePackages = {"com.example.springcloud.transaction.service", "com.example.springcloud.transaction.aspect"},
        // 自动检测@Component @Service @Repository @Controller注解
        useDefaultFilters = true,
        // includeFilters在basePackages的前提下进一步减少对候选Bean的扫描
        includeFilters = @ComponentScan.Filter(
                // OhMyAspect子类(继承或者实现)
                type = FilterType.ASSIGNABLE_TYPE,
                classes = OhMyAspect.class
        ),
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {OrderService.class, ProductionService.class}))
public class AppConfig {
}
