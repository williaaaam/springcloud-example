package com.example.springcloud.transaction.service;

import com.example.springcloud.transaction.dao.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author Williami
 * @description
 * @date 2021/9/9
 */
@Service
public class OrderService {

    @Autowired
    OrderMapper orderMapper;

    /**
     *
     */
    @Transactional(rollbackFor = Throwable.class)
    public void list() {
        orderMapper.list();
        orderMapper.add();
        orderMapper.update();
    }


}
