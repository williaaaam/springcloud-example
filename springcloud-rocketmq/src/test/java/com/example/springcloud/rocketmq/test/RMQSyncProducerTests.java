package com.example.springcloud.rocketmq.test;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author Williami
 * @description
 * @date 2021/10/14
 */
@Slf4j
public class RMQSyncProducerTests {


    private static final String NAMESRV_ADDR = "139.196.113.146:9876";

    @SneakyThrows
    @Test
    public void startProducerInSynchronously() {

        // 0.
        startConsumer();

        // 1. Instantiate with a producer group name.
        DefaultMQProducer producer = new DefaultMQProducer("order_stock_producer");
        producer.setVipChannelEnabled(false);
        // 2. Specify name server addresses.
        producer.setNamesrvAddr(NAMESRV_ADDR);

        // 3. Launch the instance.
        producer.start();
        for (int i = 0; i < 1; i++) {
            //Create a message instance, specifying topic, tag and message body.
            Message msg = new Message("TopicTest" /* Topic */,
                    "TagA" /* Tag */,
                    ("Hello RocketMQ " +
                            i).getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
            );
            // 4. Call send message to deliver message to one of brokers.
            SendResult sendResult = producer.send(msg);
            System.out.printf("%s%n", sendResult);
        }
        // 5. Shut down once the producer instance is not longer in use.
        producer.shutdown();

        TimeUnit.SECONDS.sleep(10L);
    }

    @SneakyThrows
    @Test
    public void sendMessageAsynchronously() {
        //Instantiate with a producer group name.
        DefaultMQProducer producer = new DefaultMQProducer("please_rename_unique_group_name");
        // Specify name server addresses.
        producer.setNamesrvAddr(NAMESRV_ADDR);
        //Launch the instance.
        producer.start();
        producer.setRetryTimesWhenSendAsyncFailed(0);

        int messageCount = 100;
        final CountDownLatch countDownLatch = new CountDownLatch(messageCount);
        for (int i = 0; i < messageCount; i++) {
            try {
                final int index = i;
                Message msg = new Message("Jodie_topic_1023",
                        "TagA",
                        "OrderID188",
                        "Hello world".getBytes(RemotingHelper.DEFAULT_CHARSET));
                // ??????????????????
                producer.send(msg, new SendCallback() {
                    @Override
                    public void onSuccess(SendResult sendResult) {
                        countDownLatch.countDown();
                        System.out.printf("%-10d OK %s %n", index, sendResult.getMsgId());
                    }

                    @Override
                    public void onException(Throwable e) {
                        countDownLatch.countDown();
                        System.out.printf("%-10d Exception %s %n", index, e);
                        e.printStackTrace();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        countDownLatch.await(5, TimeUnit.SECONDS);
        producer.shutdown();
    }


    @SneakyThrows
    @Test
    public void sendMessageOneWay() {

        //Instantiate with a producer group name.
        DefaultMQProducer producer = new DefaultMQProducer("please_rename_unique_group_name");
        // Specify name server addresses.
        producer.setNamesrvAddr(NAMESRV_ADDR);
        //Launch the instance.
        producer.start();
        for (int i = 0; i < 100; i++) {
            //Create a message instance, specifying topic, tag and message body.
            Message msg = new Message("TopicTest" /* Topic */,
                    "TagA" /* Tag */,
                    ("Hello RocketMQ " +
                            i).getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
            );
            //Call send message to deliver message to one of brokers.
            producer.sendOneway(msg);
        }

        //Wait for sending to complete
        Thread.sleep(5000);
        producer.shutdown();
    }

    @SneakyThrows
    @Test
    public void startConsumer() {
        // Instantiate with specified consumer group name.
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("please_rename_unique_group_name");

        // Specify name server addresses.
        consumer.setNamesrvAddr(NAMESRV_ADDR);

        // Subscribe one more more topics to consume.
        // ??????consumer????????????Topic???Tag???*???????????????Tag
        consumer.subscribe("TopicTest", "*");

        //????????????????????????consumer???????????????
        //CONSUME_FROM_LAST_OFFSET ?????????????????????????????????????????????????????????????????????
        //CONSUME_FROM_FIRST_OFFSET ???????????????????????????????????????????????????????????????broker????????????????????????
        //CONSUME_FROM_TIMESTAMP ????????????????????????????????????setConsumeTimestamp()??????????????????????????????????????????
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

        // Register callback to execute on arrival of messages fetched from brokers.
        // ????????????Listener????????????????????????????????????
        consumer.registerMessageListener(new MessageListenerConcurrently() {

            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
                                                            ConsumeConcurrentlyContext context) {
                System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(), msgs);
                // ??????????????????
                // CONSUME_SUCCESS ????????????
                // RECONSUME_LATER ???????????????????????????????????????
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        //Launch the consumer instance.
        consumer.start();

        System.out.printf("Consumer Started.%n");
    }

}
