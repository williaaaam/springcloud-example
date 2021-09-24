package com.example.springcloud.sharding.jdbc.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.springcloud.sharding.jdbc.dal.entity.OrderPO;
import com.example.springcloud.sharding.jdbc.dal.mapper.OrderPOMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class OrderService {

    @Resource
    private OrderPOMapper orderMapper;

    //列出所有订单
    public List<OrderPO> getAllOrder() {
        LambdaQueryWrapper<OrderPO> lambdaQueryWrapper = Wrappers.lambdaQuery();
        List<OrderPO> order_list = orderMapper.selectList(lambdaQueryWrapper);
        return order_list;
    }

    //添加订单
    public boolean addOneOrder(OrderPO orderOne) {
        int num = orderMapper.insert(orderOne);
        if (num == 1) {
            return true;
        } else {
            return false;
        }
    }
}