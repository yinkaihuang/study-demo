package cn.bucheng;
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

import cn.bucheng.bean.Record;
import cn.bucheng.source.RecordSource;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.functions.windowing.AllWindowFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author yinchong
 * @create 2020/2/3 16:32
 * @description
 */
public class JobStart {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        DataStreamSource<Record> stream = env.addSource(new RecordSource());
        stream.print();
        stream.assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor<Record>(Time.seconds(5)) {
            @Override
            public long extractTimestamp(Record element) {
                return element.getTime();
            }
        }).timeWindowAll(Time.seconds(30)
//        ).allowedLateness(Time.seconds(1)
        ).apply(new AllWindowFunction<Record, Record, TimeWindow>() {
            @Override
            public void apply(TimeWindow window, Iterable<Record> values, Collector<Record> out) throws Exception {
                long start = window.getStart();
                long end = window.getEnd();
                System.out.println("this window is fire:[" + parseTime(start) + " , " + parseTime(end) + "]");
                for (Record record : values) {
                    System.out.println("this fire have element:" + record);
                }
            }
        });

        env.execute("window test");

    }

    public static String parseTime(long time) {
        Date date = new Date(time);
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        return dateFormat.format(date);
    }
}
