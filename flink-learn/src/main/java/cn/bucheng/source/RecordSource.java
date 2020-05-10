package cn.bucheng.source;
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
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

import java.util.Random;

/**
 * @author yinchong
 * @create 2020/2/3 16:34
 * @description
 */
public class RecordSource extends RichSourceFunction<Record> {
    @Override
    public void run(SourceContext<Record> ctx) throws Exception {
        Random random = new Random();
        int index = 0;
        while (true) {
            int number = random.nextInt(10);
            Record.RecordBuilder builder = Record.builder();
            if (number % 2 == 0) {
                builder.time(System.currentTimeMillis() + number * 1000);
            } else {
                builder.time(System.currentTimeMillis() - number * 1000);
            }
            builder.msg("test" + (index++));
            ctx.collect(builder.build());
            Thread.sleep(2000);
        }
    }

    @Override
    public void cancel() {

    }
}
