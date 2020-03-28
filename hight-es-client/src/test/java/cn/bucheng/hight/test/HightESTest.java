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
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.open.OpenIndexRequest;
import org.elasticsearch.action.admin.indices.open.OpenIndexResponse;
import org.elasticsearch.action.admin.indices.template.delete.DeleteIndexTemplateRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.indices.CloseIndexRequest;
import org.elasticsearch.client.indices.GetIndexTemplatesRequest;
import org.elasticsearch.client.indices.GetIndexTemplatesResponse;
import org.elasticsearch.client.indices.PutIndexTemplateRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.histogram.ParsedDateHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.ParsedSum;
import org.elasticsearch.search.aggregations.metrics.SumAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
                .time(System.currentTimeMillis())
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

    /**
     * 删除数据
     */
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

    /**
     * 关闭索引
     */
    @Test
    public void testCloseIndex() {
        CloseIndexRequest request = new CloseIndexRequest("log-monitor-2020.03.28");
        try {
            AcknowledgedResponse closeIndexResponse = instance.client().indices().close(request, RequestOptions.DEFAULT);
            System.out.println(closeIndexResponse.isAcknowledged());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 开启索引
     */
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

    /**
     * 删除索引
     */
    @Test
    public void testDeleteIndex() {
        DeleteIndexRequest request = new DeleteIndexRequest("log-monitor-2020.03.28");
        try {
            AcknowledgedResponse acknowledgedResponse = instance.client().indices().delete(request, RequestOptions.DEFAULT);
            System.out.println(acknowledgedResponse.isAcknowledged());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testAggrSearch(){
        SearchSourceBuilder searchBuilder = new SearchSourceBuilder();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.matchQuery("app","ishow-sm-portal"));
        searchBuilder.query(queryBuilder);

        TermsAggregationBuilder termAggr = AggregationBuilders
                .terms("url_term")
                .field("url");

        searchBuilder.aggregation(termAggr);

        DateHistogramAggregationBuilder timeHistogram = AggregationBuilders
                .dateHistogram("time_histogram")
                .field("time")
                .calendarInterval(DateHistogramInterval.HOUR);

        termAggr.subAggregation(timeHistogram);

        SumAggregationBuilder errorSumAggr = AggregationBuilders
                .sum("error")
                .field("error");

        SumAggregationBuilder successSumAggr = AggregationBuilders
                .sum("success")
                .field("success");

        timeHistogram
                .minDocCount(1)
                .subAggregation(errorSumAggr)
                .subAggregation(successSumAggr);

        SearchRequest request = new SearchRequest();
        request.source(searchBuilder);

        try {
            SearchResponse searchResponse = instance.client().search(request, RequestOptions.DEFAULT);
            parseResponse(searchResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void parseResponse(SearchResponse response){
        Aggregations aggregations = response.getAggregations();
        if(aggregations==null){
            return ;
        }
        ParsedStringTerms urlTerm = aggregations.get("url_term");
        List<? extends Terms.Bucket> termBuckets = urlTerm.getBuckets();
        for(Terms.Bucket termBucket:termBuckets){
            String url  = termBucket.getKeyAsString();
            System.out.println(url);
            Aggregations timeHistogram = termBucket.getAggregations();
            if(timeHistogram==null){
                continue;
            }
            ParsedDateHistogram time_histogram = timeHistogram.get("time_histogram");
            List<? extends Histogram.Bucket> timeBuckets = time_histogram.getBuckets();
            for(Histogram.Bucket timeBucket:timeBuckets){
                long time = Long.parseLong(timeBucket.getKeyAsString());
                Aggregations sumAggr = timeBucket.getAggregations();
                ParsedSum errorSum = sumAggr.get("error");
                ParsedSum successSum = sumAggr.get("success");
                System.out.println(getDateString(time)+" "+errorSum.getValue()+" "+successSum.getValue());
            }
        }
    }


    private static String getDateString(long time){
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }
}
