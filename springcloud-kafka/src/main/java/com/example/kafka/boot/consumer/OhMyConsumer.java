package com.example.kafka.boot.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * @author Williami
 * @description
 * @date 2022/3/10
 */
@Component
public class OhMyConsumer {

    /**
     * @param record
     * @param ack    仅针对手动提交有效
     */
    @KafkaListener(topics = "my-replicated-topic", groupId = "defaultGroup", topicPartitions = {
            @TopicPartition(topic = "topic1", partitions = {"0", "1"}),
            @TopicPartition(topic = "topic2", partitions = "0",
                    partitionOffsets = @PartitionOffset(partition = "1", initialOffset = "100"))
    }, concurrency = "3") // concurrency就是消费组下的消费者个数，并发消费数，建议小于等于分区数
    public void listenGroup(ConsumerRecord<String, String> record, Acknowledgment ack) {
        // 手动提交offset，如果不提交说明消息会被重复消费
        System.out.println("发送的具体消息：" + record.value());
        // 手动提交
        ack.acknowledge();
    }

}
