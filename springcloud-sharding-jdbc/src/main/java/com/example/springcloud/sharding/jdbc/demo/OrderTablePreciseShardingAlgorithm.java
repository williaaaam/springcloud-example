package com.example.springcloud.sharding.jdbc.demo;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;

/**
 * @author Williami
 * @description
 * @date 2021/9/24
 */
public class OrderTablePreciseShardingAlgorithm implements PreciseShardingAlgorithm<Long> {
    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> shardingValue) {
        Long curValue = shardingValue.getValue();
        String curTable = "";
        if (curValue > 0 && curValue <= 100) {
            curTable = "t_order_6";
        } else if (curValue > 100 && curValue <= 200) {
            curTable = "t_order_7";
        } else if (curValue > 200 && curValue <= 300) {
            curTable = "t_order_8";
        } else {
            curTable = "t_order_9";
        }
        return curTable;
    }
}
