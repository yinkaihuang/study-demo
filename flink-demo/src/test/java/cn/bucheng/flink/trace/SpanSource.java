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
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

import java.util.Random;

/**
 * @author yinchong
 * @create 2020/1/31 9:50
 * @description
 */
public class SpanSource extends RichSourceFunction<String> {
    @Override
    public void run(SourceContext<String> ctx) throws Exception {
        Random random = new Random();
        while (true) {
            int number = random.nextInt(5);
            String app = "serverA";
            String traceId = "12xder5d56";
            String spanId = "span_";
            String method = "GET";
            String url = "";
            if (number % 3 == 0) {
                app = "serverA";
                traceId += "a";
                spanId = "";
                method = "POST";
                url += "/serverA";
            } else if (number % 3 == 1) {
                app = "serverB";
                traceId += "b";
                spanId += (number - 1);
                method = "DELETE";
                url += "/serverB";
            } else if (number % 3 == 2) {
                app = "serverC";
                traceId += "c";
                spanId += (number - 1);
                method = "GET";
                url += "/serverC";
            }
            url += "/user/query";
            TraceDemo.Span span = TraceDemo.Span.builder().app(app).currentId("span_" + number).parentId(spanId).method(method).url(url).traceId(traceId).build();
            ctx.collect(JSON.toJSONString(span));
            Thread.sleep(20);
        }
    }

    @Override
    public void cancel() {

    }
}
