package com.example.springcloud.sharding.jdbc.dal.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Williami
 * @description
 * @date 2021/9/24
 */
@Getter
@Setter
@ToString
@TableName("t_order")
public class OrderPO extends Model<OrderPO> {

    private Long id;

    /**
     * order_id % 2 用来分表
     */
    private Long orderId;
    /**
     * user_id % 1 用来分库
     */
    private Long userId;
    private String tradeType;
    private BigDecimal amount;
    private String currency;
    private String channel;
    private String tradeNo;
    private Date updateTime;
    private Date createTime;
    private String remark;
}
