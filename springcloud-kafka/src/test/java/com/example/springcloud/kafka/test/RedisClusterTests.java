package com.example.springcloud.kafka.test;

import com.example.kafka.util.RedisClusterUtil;
import org.junit.Test;
import redis.clients.jedis.JedisCluster;

/**
 * @author Williami
 * @description
 * @date 2022/3/14
 */
public class RedisClusterTests {

    @Test
    public void testRedisCluster() {
        JedisCluster redisCluster = RedisClusterUtil.getJedis();
        redisCluster.get("2333");
    }
}
