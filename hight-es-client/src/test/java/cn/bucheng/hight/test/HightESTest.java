package cn.bucheng.hight.test;
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

import cn.bucheng.hight.es.HightESClient;
import cn.bucheng.hight.es.domain.ServiceMonitorDTO;
import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.admin.indices.open.OpenIndexRequest;
import org.elasticsearch.action.admin.indices.open.OpenIndexResponse;
import org.elasticsearch.action.admin.indices.template.delete.DeleteIndexTemplateRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.indices.CloseIndexRequest;
import org.elasticsearch.client.indices.GetIndexTemplatesRequest;
import org.elasticsearch.client.indices.GetIndexTemplatesResponse;
import org.elasticsearch.client.indices.PutIndexTemplateRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * @author yinchong
 * @create 2020/3/28 10:30
 * @description
 */
public class HightESTest {
    private HightESClient instance;

    @Before
    public void initClient() {
        instance = HightESClient.getInstance("localhost", 9200);
    }

    /**
     * 创建模板索引
     */
    @Test
    public void createTemplateTest() {
        PutIndexTemplateRequest request = new PutIndexTemplateRequest("log-monitor");
        request.source("{\n" +
                "  \"template\": \"log-monitor-*\",\n" +
                "  \"settings\": {\n" +
                "    \"index.number_of_shards\": 1,\n" +
                "    \"number_of_replicas\": 0\n" +
                "  },\n" +
                "  \"mappings\": {\n" +
                "    \"properties\": {\n" +
                "      \"app\": {\n" +
                "        \"type\": \"keyword\"\n" +
                "      },\n" +
                "      \"url\": {\n" +
                "        \"type\": \"keyword\"\n" +
                "      },\n" +
                "      \"time\":{\n" +
                "        \"type\": \"long\"\n" +
                "      },\n" +
                "      \"method\": {\n" +
                "        \"type\": \"keyword\"\n" +
                "      },\n" +
                "      \"error\":{\n" +
                "        \"type\": \"integer\"\n" +
                "      },\n" +
                "      \"success\":{\n" +
                "        \"type\": \"integer\"\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}", XContentType.JSON);

        try {
            AcknowledgedResponse response = instance.client().indices().putTemplate(request, RequestOptions.DEFAULT);
            System.out.println(response.isAcknowledged());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 删除索引模板
     */
    @Test
    public void testDeleteTemplate() {
        DeleteIndexTemplateRequest request = new DeleteIndexTemplateRequest("log-monitor");
        try {
            AcknowledgedResponse response = instance.client().indices().deleteTemplate(request, RequestOptions.DEFAULT);
            System.out.println(response.isAcknowledged());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取模板索引
     */
    @Test
    public void testGetTemplate() {
        GetIndexTemplatesRequest request = new GetIndexTemplatesRequest("log-monitor");
        try {
            GetIndexTemplatesResponse template = instance.client().indices().getIndexTemplate(request, RequestOptions.DEFAULT);
            System.out.println(template);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 向索引中添加数据
     */
    @Test
    public void testSaveData() {
        ServiceMonitorDTO dto = ServiceMonitorDTO.builder()
                .app("ishow-sm-portal")
                .url("/sm/user/list")
                .method("get")
                .error(102)
                .success(1000)
                .build();

        IndexRequest request = new IndexRequest("log-monitor-2020.03.28");
        String jsonString = JSON.toJSONString(dto);
        request.source(jsonString, XContentType.JSON);
        try {
            IndexResponse index = instance.client().index(request, RequestOptions.DEFAULT);
            System.out.println(index);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testDeleteData() {
        DeleteRequest request = new DeleteRequest("log-monitor-2020.03.28", "a4YvH3EBBAk4x_7s9Lhn");
        try {
            DeleteResponse deleteResponse = instance.client().delete(request, RequestOptions.DEFAULT);
            System.out.println(deleteResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCloseIndex() {

    }

    @Test
    public void testOpenIndex() {
        OpenIndexRequest request = new OpenIndexRequest("log-monitor-2020.03.28");
        try {
            OpenIndexResponse openIndexResponse = instance.client().indices().open(request, RequestOptions.DEFAULT);
            System.out.println(openIndexResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testDeleteIndex() {
        CloseIndexRequest request = new CloseIndexRequest("log-monitor-2020.03.28");
        try {
            AcknowledgedResponse closeIndexResponse = instance.client().indices().close(request, RequestOptions.DEFAULT);
            System.out.println(closeIndexResponse.isAcknowledged());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
