package com.example.springcloud.transaction.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author Williami
 * @description
 * @date 2021/9/16
 */
public class TransactionUtil {

    /**
     * 资源同步
     */
    private static final ThreadLocal<Connection> CONNECTION_THREAD_LOCAL = new ThreadLocal<>();


    public static Connection start() throws SQLException {
        Connection connection = CONNECTION_THREAD_LOCAL.get();
        if (connection == null) {
            connection = DriverManager.getConnection("jdbc:mysql://139.196.113.146:3306/op_sp?useUnicode=true&characterEncoding=UTF-8&serverTimezone=CTT", "root", "Monday01");
            CONNECTION_THREAD_LOCAL.set(connection);
            // 关闭自动提交
            connection.setAutoCommit(false);
        }
        return connection;
    }

    public static int doInTransaction(String sql,Object...args){
        Connection connection = startTransaction();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            if (args != null) {
                for (int i = 1; i < args.length + 1; i++) {
                    preparedStatement.setObject(i, args[i - 1]);
                }
            }
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    private TransactionUtil() {
    }

}
