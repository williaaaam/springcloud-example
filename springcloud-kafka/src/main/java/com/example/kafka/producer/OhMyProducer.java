package com.example.kafka.producer;

import com.example.kafka.MyPartitioner;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * @author Williami
 * @description
 * @date 2022/3/9
 */
public class OhMyProducer {

    private static final String TOPIC_NAME = "my-replicated-topic";


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 1. 设置参数
        Properties properties = new Properties();
        // 连接Kafka集群
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "ip:port,ip2:port2");
        // key从字符串序列化为字节数组
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // 自定义分区器
        properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, MyPartitioner.class.getName());
        // leader成功写入日志文件才会返回ack给生产者
        properties.put(ProducerConfig.ACKS_CONFIG, 1);

        // 缓冲区大小,默认32m
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 335544432);

        // 批次大小,默认16k
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);

        // linger.ms 等待时间，默认0
        properties.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        // 压缩
        /**
         * 压缩默认none,可配置值：gzip, snappy,lz4,zstd
         */
        properties.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");

        // ack
        properties.put(ProducerConfig.ACKS_CONFIG, "1");

        properties.put(ProducerConfig.RETRIES_CONFIG, 3);

        // 幂等,默认true
        properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true); // boolean类型


        properties.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "transactionId");


        // 2. 生产消息客户端
        Producer<String, String> producer = new KafkaProducer<String, String>(properties);
        // 3. 创建消息
        // key决定消息往topic哪个分区上发，value是具体要发送的消息内容
        // key和partition优先级? partition优先级更高
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(TOPIC_NAME, 0, "oh-my-key", "helloKafka");
        // 4. 发送消息
        //RecordMetadata recordMetadata = producer.send(producerRecord).get();

        //System.out.println("同步方式发送消息结果" + recordMetadata.topic() + "|" + recordMetadata.partition() + "|" + recordMetadata.offset());

        producer.initTransactions();

        producer.beginTransaction();
        // 4. 异步发送消息
        try {
            producer.send(producerRecord, (metadata, exception) -> {
                if (exception != null) {
                    System.out.println("发送消息失败" + exception.getStackTrace());
                }
                if (metadata != null) { // callback正常返回
                    // 打印 主题 分区 偏移量
                    System.out.println("异步方式发送消息结果" + metadata.topic() + metadata.partition() + metadata.offset());
                }
            });
            // 提交事务
            producer.commitTransaction();
        } catch (Exception e) {
            // 中止事务
            producer.abortTransaction();
        } finally {
            producer.close();
        }


    }


}
