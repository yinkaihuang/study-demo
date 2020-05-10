package cn.bucheng.flink.checkpoint;
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

import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.junit.Test;

import java.util.Random;

/**
 * @author yinchong
 * @create 2020/1/30 14:51
 * @description
 */
public class CheckPointTest {

    @Test
    public void checkPointTest()throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        env.enableCheckpointing(6000);
        CheckpointConfig checkpointConfig = env.getCheckpointConfig();
        checkpointConfig.setMinPauseBetweenCheckpoints(2000);
        checkpointConfig.setCheckpointTimeout(1000);
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        checkpointConfig.enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);

//        env.fromElements( 2L, 5L, 10L, 23L, 11L, 6L,1L)
        env.addSource(new RichSourceFunction<Long>() {
            @Override
            public void run(SourceContext<Long> ctx) throws Exception {
                Random random = new Random();
                while(true){
                    int number = random.nextInt(10);
                    ctx.collect((long) number);
                    Thread.sleep(400);
                }
            }

            @Override
            public void cancel() {

            }
        })
                .flatMap(new CountOperatorState())
                .addSink(new SinkFunction<String>() {
                    @Override
                    public void invoke(String value) throws Exception {
                        System.out.println(value);
                    }
                });

        env.execute("check point test");
    }

}
