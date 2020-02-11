package cn.bucheng.flink.trace;
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

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * @author yinchong
 * @create 2020/1/29 18:34
 * @description
 */
public class TraceDemo {

    @Test
    public void traceTest() throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStream<String> stream = env.addSource(new SpanSource());
        SingleOutputStreamOperator<Set<Span>> traceStream = stream.map(new MapFunction<String, Span>() {
            @Override
            public Span map(String value) throws Exception {
                return JSON.parseObject(value, Span.class);
            }
        }).keyBy(new KeySelector<Span, String>() {
            @Override
            public String getKey(Span value) throws Exception {
                return value.traceId;
            }
        }).timeWindowAll(Time.minutes(5), Time.minutes(1)
        ).aggregate(new AggregateFunction<Span, Set<Span>, Set<Span>>() {
            @Override
            public Set<Span> createAccumulator() {
                return new HashSet<>();
            }

            @Override
            public Set<Span> add(Span value, Set<Span> accumulator) {
                if (value != null) {
                    accumulator.add(value);
                }
                return accumulator;
            }

            @Override
            public Set<Span> getResult(Set<Span> accumulator) {
                return accumulator;
            }

            @Override
            public Set<Span> merge(Set<Span> a, Set<Span> b) {
                if (a == null) {
                    a = new HashSet<>();
                }
                a.addAll(b);
                return a;
            }
        }).filter(new FilterFunction<Set<Span>>() {
            @Override
            public boolean filter(Set<Span> value) throws Exception {
                StringBuilder sb = new StringBuilder();
                for (Span span : value) {
                    sb.append(span.getUrl()).append("-").append(span.getMethod()).append("-").append(span.getApp());
                }
                return RedisBloomFilter.getInstance("127.0.0.1",6379).add(sb.toString());
            }
        });


        traceStream.print();

        env.execute("trace flink job");

    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Span {
        //当前事务ID
        String traceId;
        //上一个节点ID
        String parentId;
        //当前节点ID
        String currentId;
        //服务名称
        String app;
        //方法
        String method;
        //请求方法URL
        String url;
    }


}
