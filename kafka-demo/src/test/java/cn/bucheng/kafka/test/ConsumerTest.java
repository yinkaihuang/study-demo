package cn.bucheng.kafka.test;
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.kafka.clients.consumer.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

/**
 * @author yinchong
 * @create 2020/3/29 17:05
 * @description
 */
public class ConsumerTest {

    private Consumer<String, String> consumer;

    @Before
    public void init() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        //设置消费所属的组
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "test_kafka_group");
        //设置手动提交模式
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        //设置第一次注册到zookeeper读取消息策略 earliest,latest,none
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        //设置自动提交的时间间接
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        //设置每次拉取的时间为2秒
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 5000);
        //设置每次最大拉取的记录数量为2条
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 2);
        //设置获取到键反序列化模式
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        //设置获取到值反序列化模式
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        consumer = new KafkaConsumer<>(props);
    }

    @After
    public void close() {
        consumer.close();
    }

    /**
     * CommitFailedException
     *
     * @throws InterruptedException
     */
    @Test
    public void testConsume() throws InterruptedException {
        consumer.subscribe(Arrays.asList("test-kafka-topic"));
        for (int i = 0; i < 10; i++) {
            ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofSeconds(1));
            for (ConsumerRecord<String, String> record : consumerRecords) {
                System.out.println(record);
                //消息的处理时间大于max.poll.interval.ms时间
                Thread.sleep(1000 * 10);
                consumer.commitSync();
            }
        }
    }
}
