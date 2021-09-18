package com.example.springcloud.transaction.service;

import com.example.springcloud.transaction.annotation.OhMyTransactional;
import com.example.springcloud.transaction.utils.TransactionUtil;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

/**
 * @author Williami
 * @description
 * @date 2021/9/16
 */
@Service
public class OhMyService {

    @OhMyTransactional
    public boolean update() {
        String sql = "update tb_op_order set order_type = 'P',update_time=now() where id = 1";
        try {
            TransactionUtil.doInTransaction(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

}
