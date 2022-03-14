package com.example.kafka.util;

/**
 * @author Williami
 * @description
 * @date 2022/3/14
 */

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by LiChao on 2019/2/16
 */
public class RedisClusterUtil {

    private static JedisCluster jedis = null;

    // 最大连接数
    private static Integer MAX_TOTAL = 8;
    // 最大空闲连接数
    private static Integer MAX_IDLE = 8;
    // 等待可用连接的最大时间，单位是毫秒，默认值为-1，表示永不超时。
    // 如果超过等待时间，则直接抛出JedisConnectionException
    // 获取连接时的最大等待毫秒数,小于零:阻塞不确定的时间,默认-1
    private static Integer MAX_WAIT_MILLIS = 10000;

    // 在获取连接的时候检查有效性, 默认false
    private static Boolean TEST_ON_BORROW = true;
    // 在空闲时检查有效性, 默认false
    private static Boolean TEST_WHILE_IDLE = true;
    // 在归还给pool时，是否提前进行validate操作
    private static Boolean TEST_ON_RETURN = true;

    //访问密码
    private static String AUTH = null;

    /**
     * 静态块，初始化Redis连接池
     */
    static {
        try {
            // 连接池配置
            JedisPoolConfig config = new JedisPoolConfig();
            /*注意：
                在高版本的jedis jar包，比如本版本2.9.0，JedisPoolConfig没有setMaxActive和setMaxWait属性了
                这是因为高版本中官方废弃了此方法，用以下两个属性替换。
                maxActive  ==>  maxTotal
                maxWait==>  maxWaitMillis
             */
            config.setMaxTotal(MAX_TOTAL);
            config.setMaxIdle(MAX_IDLE);
            config.setMaxWaitMillis(MAX_WAIT_MILLIS);
            config.setTestOnBorrow(TEST_ON_BORROW);
            config.setTestWhileIdle(TEST_WHILE_IDLE);
            config.setTestOnReturn(TEST_ON_RETURN);

            Set<HostAndPort> jedisClusterNode = new HashSet<HostAndPort>();
            jedisClusterNode.add(new HostAndPort("101.35.19.88", 6380));
            jedisClusterNode.add(new HostAndPort("101.35.19.88", 6381));
            jedisClusterNode.add(new HostAndPort("101.35.19.88", 6382));
            jedisClusterNode.add(new HostAndPort("101.35.19.88", 6383));
            jedisClusterNode.add(new HostAndPort("101.35.19.88", 6384));
            jedisClusterNode.add(new HostAndPort("101.35.19.88", 6385));

            jedis = new JedisCluster(jedisClusterNode, 10000, 10000, 5, AUTH, config);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static JedisCluster getJedis() {
        return jedis;
    }
}


