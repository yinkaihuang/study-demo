package cn.bucheng.flink.splite;
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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.flink.streaming.api.collector.selector.OutputSelector;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SplitStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * @author yinchong
 * @create 2020/2/2 17:11
 * @description
 */
public class SpliteSelectDemo {

    @Test
    public void splitSelectTest()throws Exception{
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<Record> stream = env.addSource(new RecordDataSource());
//        stream.print();
        SplitStream<Record> splitStream = stream.split(new OutputSelector<Record>() {
            @Override
            public Iterable<String> select(Record value) {
                List<String> datas = new LinkedList<>();
                datas.add(value.getBizId());
                return datas;
            }
        });

        splitStream.select("1").print();
        env.execute("split select test");
    }

    static class RecordDataSource extends RichSourceFunction<Record>{

        @Override
        public void run(SourceContext<Record> ctx) throws Exception {
            Random random = new Random();
            while(true){
                int number = random.nextInt(5);
                Record record = Record.builder().bizId("" + number).price(number + 10).type(number)
                        .recordId(UUID.randomUUID().toString()).build();
                ctx.collect(record);
                Thread.sleep(100);
            }
        }

        @Override
        public void cancel() {

        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Record{
        private String bizId;
        private String recordId;
        private Integer price;
        private Integer type;
        private String remark;
    }
}
