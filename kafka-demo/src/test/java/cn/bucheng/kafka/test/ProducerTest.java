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

import cn.bucheng.kafka.domain.ServerTrace;
import com.alibaba.fastjson.JSON;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

/**
 * @author yinchong
 * @create 2020/3/29 17:05
 * @description
 */
public class ProducerTest {
    private Producer<String, String> producer;

    @Before
    public void init() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        //配置接收模式为需要所有broken确认
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        //设置消息发送失败重试次数
        props.put(ProducerConfig.RETRIES_CONFIG, 3);
        //设置批量发送数据大小
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        //设置延迟时间
        props.put(ProducerConfig.LINGER_MS_CONFIG, 10);
        //设置内存缓存大小
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        //设置消息发送是键采用的序列化模式
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        //设置消息发送是值采用的序列化模式
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        producer = new KafkaProducer<>(props);
    }

    @After
    public void close() {
        producer.close();
    }


    @Test
    public void testSend() {
        String topic = "test-kafka-topic";
        for (int i = 0; i < 10; i++) {
            ServerTrace trace = ServerTrace.builder().app("ishow-teacher").url("/ishow/usr/list").traceId("x244i8dkf" + i).time(System.currentTimeMillis()).build();
            String content = JSON.toJSONString(trace);
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, content);
            producer.send(record);
        }
    }
}
