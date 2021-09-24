package com.example.springcloud.sharding.jdbc.test;

import com.example.springcloud.sharding.jdbc.App;
import com.example.springcloud.sharding.jdbc.dal.entity.OrderPO;
import com.example.springcloud.sharding.jdbc.dal.mapper.OrderPOMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Williami
 * @description
 * @date 2021/9/24
 */
@SpringBootTest(classes = App.class)
public class AppTests {


    @Autowired
    private OrderPOMapper orderMapper;


    @Test
    public void insert() {
        OrderPO order = new OrderPO();
        order.setOrderId(2L);
        order.setUserId(10L);
        order.setChannel("MIFunds-" + ThreadLocalRandom.current().nextInt(100));
        order.setTradeType("PURCHASE");

        OrderPO order2 = new OrderPO();
        order2.setOrderId(3L);
        order2.setUserId(1L);
        order2.setChannel("JD-" + ThreadLocalRandom.current().nextInt(100));
        order2.setTradeType("REDEEM");

        orderMapper.insert(order);
        orderMapper.insert(order2);

    }
}
