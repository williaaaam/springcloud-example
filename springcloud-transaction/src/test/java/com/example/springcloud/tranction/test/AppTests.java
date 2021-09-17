package com.example.springcloud.tranction.test;

import com.example.springcloud.transaction.service.OrderService;
import com.example.springcloud.transaction.service.ProductionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Williami
 * @description
 * @date 2021/9/9
 */
@SpringBootTest(classes = com.example.springcloud.transaction.App.class)
public class AppTests {

    @Autowired
    OrderService orderService;

    @Autowired
    ProductionService productionService;

    @Test
    public void op() {
        orderService.list();
    }


    @Test
    public void transaction(){
        productionService.doSth();
    }

}
