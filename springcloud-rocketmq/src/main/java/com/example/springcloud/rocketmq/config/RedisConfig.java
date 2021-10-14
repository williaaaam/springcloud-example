package com.example.springcloud.rocketmq.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;

/**
 * @author
 */
@Slf4j
@Configuration
public class RedisConfig {


//    /**
//     *
//     */
//    @Value("${spring.redis.password}")
//    private String password;

    @Data
    static class MyRedisClusterProp {

        /**
         *
         */
        private Integer maxRedirects;

        /**
         *
         */
        private List<String> nodes;

    }

//    @Profile({"staging", "c3", "c4"})
//    @Bean
//    @ConfigurationProperties("spring.redis.cluster")
//    public MyRedisClusterProp myRedisClusterProp() {
//        return new MyRedisClusterProp();
//    }
//
//    @Profile({"staging", "c3", "c4"})
//    @Bean
//    public RedisClusterConfiguration redisClusterConfiguration(KeyCenterAgent keyCenterAgent) throws CryptException {
//        try {
//            this.password = keyCenterAgent.decrypt(this.password);
//        } catch (CryptException e) {
//            log.error("keycenter cryptor error", e);
//            throw e;
//        }
//
//        MyRedisClusterProp prop = myRedisClusterProp();
//        RedisClusterConfiguration redisClusterConfiguration
//                = new RedisClusterConfiguration(prop.nodes);
//
//        redisClusterConfiguration.setPassword(RedisPassword.of(this.password));
//        redisClusterConfiguration.setMaxRedirects(prop.maxRedirects);
//        return redisClusterConfiguration;
//    }

    /**
     * RedisTemplate配置
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        // 设置序列化
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(
                Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        // 配置redisTemplate
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        RedisSerializer<?> stringSerializer = new StringRedisSerializer();
        // key序列化
        redisTemplate.setKeySerializer(stringSerializer);
        // value序列化
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        // Hash key序列化
        redisTemplate.setHashKeySerializer(stringSerializer);
        // Hash value序列化
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * RedisTemplate配置
     */
    @Bean
    public RedisTemplate<String, String> redisStringTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        // 设置序列化
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(
                Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        // 配置redisTemplate
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        RedisSerializer<?> stringSerializer = new StringRedisSerializer();
        // key序列化
        redisTemplate.setKeySerializer(stringSerializer);
        // value序列化
        redisTemplate.setValueSerializer(stringSerializer);
        // Hash key序列化
        redisTemplate.setHashKeySerializer(stringSerializer);
        // Hash value序列化
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

}
