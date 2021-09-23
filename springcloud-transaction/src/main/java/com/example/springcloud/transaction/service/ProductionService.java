package com.example.springcloud.transaction.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * @author Williami
 * @description
 * @date 2021/9/16
 */
@Service
public class ProductionService {

    // 编程式事务
    private final TransactionTemplate transactionTemplate;

    private final JdbcTemplate jdbcTemplate;

    /**
     * Spring容器自动注入PlatformTransactionManager和JdbcTemplate
     * @param platformTransactionManager
     * @param jdbcTemplate
     */
    public ProductionService(PlatformTransactionManager platformTransactionManager,JdbcTemplate jdbcTemplate) {
        this.transactionTemplate = new TransactionTemplate(platformTransactionManager);
        this.jdbcTemplate = jdbcTemplate;
    }

    public Object doSth() {
        return transactionTemplate.execute((TransactionCallback<Object>) status -> update());
    }

    private boolean update() {
        String sql = "update tb_op_order set order_type = 'P',update_time=now() where id = 1";
        jdbcTemplate.execute(sql);
        return Boolean.FALSE;
    }

}
