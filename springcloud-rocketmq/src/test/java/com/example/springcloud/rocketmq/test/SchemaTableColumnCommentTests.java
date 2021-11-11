package com.example.springcloud.rocketmq.test;


import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author Williami
 * @description
 * @date 2021/11/2
 */
@Slf4j
public class SchemaTableColumnCommentTests {

    @SneakyThrows
    private static Connection getConnection() {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mysql://your_ip:port/db_name?useUnicode=true&characterEncoding=UTF-8&serverTimezone=CTT&useSSL=false&nullCatalogMeansCurrent=true", "mysql", "mysql");
        return conn;
    }


    /**
     * 获取当前数据库下的所有表名称
     *
     * @return
     * @throws Exception
     */
    private static List queryAllTableNamesWithSchema() throws Exception {
        List tables = new ArrayList();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SHOW TABLES ");) {
            while (rs.next()) {
                String tableName = rs.getString(1);
                tables.add(tableName);
            }
        }
        return tables;
    }

    private void getColumnCommentByTableNameAndWrite(List tableName, String filePath) throws Exception {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath))) {
            Map<String, List<String>> dataLevel2CommentMap = new HashMap<>(8);
            for (int i = 0; i < tableName.size(); i++) {
                String table = (String) tableName.get(i);
                ResultSet rs = stmt.executeQuery("show full columns from " + table);
                Stream.of(DATA_LEVEl.values())
                        .forEach(data_levEl -> {
                            String dataLevelKey = data_levEl.name();
                            if (!dataLevel2CommentMap.containsKey(dataLevelKey)) {
                                List<String> commentList = new ArrayList<>(64);
                                dataLevel2CommentMap.put(dataLevelKey, commentList);
                            }
                        });
                while (rs.next()) {
                    String columnComment = rs.getString("Comment");
                    if (columnComment.startsWith(DATA_LEVEl.PII.name())) {
                        dataLevel2CommentMap.get(DATA_LEVEl.PII.name()).add(columnComment.substring(DATA_LEVEl.PII.name().length() + 1));
                        continue;
                    }
                    if (columnComment.startsWith(DATA_LEVEl.SPII.name())) {
                        dataLevel2CommentMap.get(DATA_LEVEl.SPII.name()).add(columnComment.substring(DATA_LEVEl.SPII.name().length() + 1));
                        continue;
                    }

                    if (columnComment.startsWith("Non-PILL")) {
                        dataLevel2CommentMap.get(DATA_LEVEl.Non_PII.name()).add(columnComment.substring(DATA_LEVEl.Non_PII.name().length() + 1));
                        continue;
                    }
                    dataLevel2CommentMap.get(DATA_LEVEl.Non_PII.name()).add(columnComment);
                }

                rs.close();

                // 部门
                bufferedWriter.write("理财\t");

                // 负责人
                bufferedWriter.write("周新建\t");

                // 表名/文件名

                bufferedWriter.write("cashbase_lc_oss." + table + "\t");


                // 存储文件地址

                bufferedWriter.write("c3-pdl.mipay_service.cashfund-cashbase_lc_oss\t");

                // 存储方式
                bufferedWriter.write("mysql\t");

                // 数据报名级别
                bufferedWriter.write("内部级C\t");


                // 访问控制方案
                bufferedWriter.write("通过系统查看信息\t");

                // 保存期限
                bufferedWriter.write("永久备份\t");


                // 备份方案
                bufferedWriter.write("实时备份\t");

                // 用户注销方案
                bufferedWriter.write("无\t");


                // PII以及加密方案
                writeCmt2File(DATA_LEVEl.PII, dataLevel2CommentMap, bufferedWriter);

                // SPII以及加密方案

                writeCmt2File(DATA_LEVEl.SPII, dataLevel2CommentMap, bufferedWriter);

                // Non-PII
                writeCmt2File(DATA_LEVEl.Non_PII, dataLevel2CommentMap, bufferedWriter);

                // 是否去标识化
                bufferedWriter.write("否\t");

                bufferedWriter.write(System.lineSeparator());

                // do write
                /*dataLevel2CommentMap.entrySet().forEach((entry) -> {
                    String key = entry.getKey();
                    try {
                        bufferedWriter.write(key + ":");
                        bufferedWriter.write(System.lineSeparator());
                        List<String> column = entry.getValue();
                        column.stream().forEach(s -> {
                            try {
                                bufferedWriter.write(s);
                                bufferedWriter.write(System.lineSeparator());
                            } catch (IOException e) {
                                log.error("", e);
                            }
                        });
                    } catch (IOException e) {
                        log.error("", e);
                    }
                });*/

                // clear map
                dataLevel2CommentMap.clear();
            }
            // clear buffer cache
            bufferedWriter.flush();
        }

    }

    @SneakyThrows
    private static void writeCmt2File(DATA_LEVEl data_levEl, Map<String, List<String>> dataLevel2CommentMap, BufferedWriter bufferedWriter) {
        List<String> strings = dataLevel2CommentMap.get(data_levEl.name());
        if (CollectionUtils.isEmpty(strings)) {
            bufferedWriter.write("无\t");
            if (data_levEl == DATA_LEVEl.SPII || data_levEl == DATA_LEVEl.PII) {
                // 加密方案
                bufferedWriter.write("无\t");
            }
        } else {
            // ==================================================
            bufferedWriter.write("\"");
            strings.stream().forEach(columnCmt -> {
                try {
                    if (StringUtils.isNotBlank(columnCmt)) {
                        // Todo: return 提前返回？ cashbase_lc_oss.tb_bankcard_replacement_info
                        bufferedWriter.write(columnCmt + "\r\n");
                    }
                } catch (IOException e) {
                    log.error("", e);
                }
            });
            bufferedWriter.write("\"\t");
            // ==================================================


            if (data_levEl == DATA_LEVEl.SPII || data_levEl == DATA_LEVEl.PII) {
                // 加密方案
                bufferedWriter.write("keycenter/AES\t");
            }
        }
    }


    @SneakyThrows
    @Test
    public void writeSQLColumnComment2LocalFileForEachTable() {
        String filePath = null;
        getColumnCommentByTableNameAndWrite(queryAllTableNamesWithSchema(), filePath);
    }

    enum DATA_LEVEl {
        SPII,
        PII,
        Non_PII,
    }

}
