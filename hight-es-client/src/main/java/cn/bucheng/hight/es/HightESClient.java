package cn.bucheng.hight.es;
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

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;

/**
 * @author yinchong
 * @create 2020/3/28 10:05
 * @description
 */
public class HightESClient {
    private RestHighLevelClient client;
    private static volatile HightESClient instance;

    private HightESClient(String ip, int port) {
        RestClientBuilder builder = RestClient.builder(new HttpHost(ip, port));
        client = new RestHighLevelClient(
                builder);
    }

    public static HightESClient getInstance(String ip, int port) {
        if (instance != null) {
            return instance;
        }
        synchronized (HightESClient.class) {
            if (instance == null) {
                instance = new HightESClient(ip, port);
            }
        }
        return instance;
    }

    public RestHighLevelClient client() {
        return client;
    }


    public void close() {
        try {
            client.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
