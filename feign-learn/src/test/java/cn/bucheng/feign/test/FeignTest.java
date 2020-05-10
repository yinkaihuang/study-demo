package cn.bucheng.feign.test;
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

import cn.bucheng.feign.learn.service.FeignService;
import feign.Feign;
import feign.Request;
import feign.RetryableException;
import feign.Retryer;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import org.junit.Test;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author yinchong
 * @create 2020/5/10 10:10
 * @description
 */
public class FeignTest {

    @Test
    public void normalFeignTest(){
        FeignService feignService = Feign.builder()
                .decoder(new GsonDecoder())
                .target(FeignService.class, "http://127.0.0.1:8080");
        Map<String, Object> map = feignService.test();
        System.out.println(map);
    }

    @Test
    public void normalFeignTimeOutTest(){
        Request.Options options = new Request.Options(5, TimeUnit.SECONDS,6,TimeUnit.SECONDS,true);
        FeignService feignService = Feign.builder()
                .options(options)
                .encoder(new GsonEncoder())
                .retryer(new MyRetryer())
                .decoder(new GsonDecoder())
                .target(FeignService.class, "http://127.0.0.1:8080");

        Map<String, Object> map = feignService.timeout(10);
        System.out.println(map);
    }

    static class MyRetryer implements Retryer{

        @Override
        public void continueOrPropagate(RetryableException e) {
            throw e;
        }

        @Override
        public Retryer clone() {
            return this;
        }
    }
}
