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
            connection = DriverManager.getConnection("jdbc:mysql://139.196.113.146:3306/op_sp?useUnicode=true&characterEncoding=UTF-8&serverTimezone=CTT", "william", "Monday01");
            CONNECTION_THREAD_LOCAL.set(connection);
            // 关闭自动提交
            connection.setAutoCommit(false);
        }
        System.out.println("****************************开始事务，并关闭自动提交******************************");
        return connection;
    }

    public static int doInTransaction(String sql, Object... args) throws SQLException {
        Connection connection = start();
        System.out.println("****************************开始执行业务逻辑******************************");
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            if (args != null) {
                for (int i = 1; i < args.length + 1; i++) {
                    preparedStatement.setObject(i, args[i - 1]);
                }
            }
            System.out.println("****************************业务逻辑执行结束******************************");
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void commit() {
        try (Connection connection = CONNECTION_THREAD_LOCAL.get();) {
            connection.commit();
            System.out.println("****************************提交事务******************************");
            CONNECTION_THREAD_LOCAL.remove();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void rollback() {
        try (Connection connection = CONNECTION_THREAD_LOCAL.get();) {
            connection.rollback();
            System.out.println("****************************事务回滚****************************");
            CONNECTION_THREAD_LOCAL.remove();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    private TransactionUtil() {
    }

}
