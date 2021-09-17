package com.example.springcloud.transaction.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Williami
 * @description
 * @date 2021/9/9
 */
@Repository
public class OrderMapper {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    /**
     *
     */
    @Transactional(rollbackFor = Throwable.class)
    public void list() {
        jdbcTemplate.execute("select * from tb_op_order");
    }

    @Transactional(rollbackFor = Throwable.class)
    public void add() {
        String sql = "insert into tb_op_order(order_no,create_time,update_time) values(" + System.currentTimeMillis() + ",now(),now())";
        jdbcTemplate.execute(sql);
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.NESTED)
    public void update() {
        String sql = "update tb_op_order set order_type = 'P',update_time=now() where id = 1";
        //jdbcTemplate.execute(sql);
        throw new RuntimeException("更新异常");
    }
}
