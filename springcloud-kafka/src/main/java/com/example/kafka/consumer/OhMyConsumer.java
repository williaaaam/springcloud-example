package com.example.kafka.consumer;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * @author Williami
 * @description
 * @date 2022/3/9
 */
public class OhMyConsumer {

    private static final String TOPIC_NAME = "my-replicated-topic";
    private static final String CONSUMER_GROUP_NAME = "testGroup1234";


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 1. 设置参数
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "ip:port,ip2:port2");
        // 消费分组名
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, StringSerializer.class.getName());
        // key从字符串序列化为字节数组
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        // 设置offset自动或者手动提交
        // 手动提交，默认true
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        // 默认自动提交时间间隔
        properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");

        // consumer给broker发送心跳的间隔时间
        properties.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, 1000);
        // kafka如果超过10s没有收到消费者的心跳，则会把消费者踢出消费组，进行rebalance,把分区分配给其他消费者
        properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 10 * 1000);
        // 一次poll最大拉取消息的条数，可以根据消费速度的快慢来设置
        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 500);
        // 如果两次poll的时间超过了30s的时间间隔，kafka会认为消费能力太弱，将其踢出消费组，将分区分配给其他消费者 -rebalance
        properties.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 30 * 1000);
        // 当消费主题的是一个新的消费组或者指定offset的消费方式，offset不存在，那么应该如何消费？
        // latest默认，只消费自己启动之后发送到主题的消息
        // earliest第一次从头开始消费，重启以后按照消费offset记录继续消费，需要区别consumer.seekToBeginning(每次都从头开始消费)
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // 2. 创建消费者的客户端
        Consumer<String, String> consumer = new KafkaConsumer<String, String>(properties);

        // 消费者订阅主题列表，可以订阅多个主题
        consumer.subscribe(Arrays.asList(TOPIC_NAME));
        // 消费者指定主题分区消费，例如主题的0号分区
        //consumer.assign(Arrays.asList(new TopicPartition(TOPIC_NAME, 0)));
        // 消费者回溯消费:消费0-offse之间的消费
        //consumer.seekToBeginning(Arrays.asList(new TopicPartition(TOPIC_NAME, 0)));
        //consumer.seek(new TopicPartition(TOPIC_NAME, 0), 10);

        // 拿到主题所有分区
        /*List<PartitionInfo> partitionInfos = consumer.partitionsFor(TOPIC_NAME);
        // 从1小时前开始消费
        long fetchDataBeginTime = System.currentTimeMillis() - 1000 * 60 * 60;
        Map<TopicPartition, Long> map = new HashMap<>();
        for (PartitionInfo partitionInfo : partitionInfos) {
            map.put(new TopicPartition(TOPIC_NAME, partitionInfo.partition()), fetchDataBeginTime);
        }
        // 根据时间拿到偏移量
        Set<Map.Entry<TopicPartition, OffsetAndTimestamp>> entrySet = consumer.offsetsForTimes(map).entrySet();
        for (Map.Entry<TopicPartition, OffsetAndTimestamp> entry : entrySet) {
            TopicPartition key = entry.getKey();
            // fetchDataBeginTime对应的offset
            OffsetAndTimestamp value = entry.getValue();
            if (key == null || value == null) {
                continue;
            }
            Long offset = value.offset();
            // 消费者指定主题分区消费，例如主题的0号分区
            consumer.assign(Arrays.asList(key));
            // 回溯消费
            consumer.seek(key, offset);
        }*/


        while (true) {
            // poll是拉取消息的长轮询,如果是自动提交的话，poll消息会自动提交
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
            records.forEach((record) -> {
                System.out.println(record.partition() + " " + record.offset() + " " + record.key() + " " + record.value());
            });

            // 所有的消息已经消费完
            if (records.count() > 0) { // 有消息
                // 手动同步提交offset,当前线程会阻塞直到offset提交成功
                // 一般使用同步提交，因为提交之后一般也没有什么逻辑代码了
                consumer.commitSync(); // 阻塞直到Kafka集群返回ack

                // 手动异步提交offset，当前线程提交offset不会阻塞，可以继续处理后面的程序逻辑
                /*consumer.commitAsync((offsets, exception) -> {
                    if (exception != null) {
                        System.out.println(offsets);
                        System.out.println(exception.getStackTrace());
                    }
                });*/
            }
        }

    }


}
