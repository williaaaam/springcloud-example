package com.example.springcloud.transaction.config;

import com.example.springcloud.transaction.aspect.OhMyAspect;
import com.example.springcloud.transaction.service.OrderService;
import com.example.springcloud.transaction.service.ProductionService;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * @author Williami
 * @description
 * @date 2021/9/18
 */
// exposeProxy=true表示将代理类暴露到线程上下文中
@EnableAspectJAutoProxy(exposeProxy = true) // 注解对@Transactional无效
// 需要注意的是，@EnableTransactionManagement的proxyTargetClass会影响Spring中所有通过自动代理生成的对象。如果将proxyTargetClass设置为true，那么意味通过@EnableAspectJAutoProxy所生成的代理对象也会使用cglib进行代理
@EnableTransactionManagement(proxyTargetClass = false) // @Transactional有效
// proxyBeanMethods=false,不对配置类生成代理对象
@Configuration(proxyBeanMethods = true)
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

    /**
     * 数据源
     *
     * @return
     */
    @Bean
    public DataSource dataSource() {
        BasicDataSource basicDataSource = new BasicDataSource();
        // MySQL 6及之后，驱动类无需手动注册，会通过SPI自动注册
        // 并且url需要显示配置serverTimezone，否则报错
        //basicDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        basicDataSource.setUrl("jdbc:mysql://139.196.113.146:3306/op_sp?useUnicode=true&characterEncoding=UTF-8&serverTimezone=CTT");
        basicDataSource.setUsername("william");
        basicDataSource.setPassword("Monday01");
        return basicDataSource;
    }

    /**
     * 事务管理器
     *
     * @return
     */
    @Bean
    public TransactionManager transactionManager() {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dataSource());
        return dataSourceTransactionManager;
    }

}
