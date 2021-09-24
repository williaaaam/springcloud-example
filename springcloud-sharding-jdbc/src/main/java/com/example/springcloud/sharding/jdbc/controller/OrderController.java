package com.example.springcloud.sharding.jdbc.controller;

import com.example.springcloud.sharding.jdbc.dal.entity.OrderPO;
import com.example.springcloud.sharding.jdbc.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Resource
    private OrderService orderService;

    //添加订单，参数：订单id/订单商品名字
    @RequestMapping("/added")
    @ResponseBody
    public String added(@RequestParam(value = "orderId", required = false, defaultValue = "0") Long orderId,
                        @RequestParam(value = "userId", required = false, defaultValue = "") Long userId) {
        OrderPO orderOne = new OrderPO();
        orderOne.setOrderId(orderId);
        orderOne.setUserId(userId);
        boolean isAdd = orderService.addOneOrder(orderOne);
        return String.valueOf(isAdd);
    }

    //添加订单的form页面
    @RequestMapping("/add")
    public String add() {
        return "order/add";
    }
}