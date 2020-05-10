package cn.bucheng.feign.learn;
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

import cn.bucheng.feign.learn.domain.Contributor;
import cn.bucheng.feign.learn.service.GitHub;
import feign.Feign;
import feign.gson.GsonDecoder;
import feign.hystrix.HystrixFeign;
import feign.ribbon.RibbonClient;

import java.util.List;

/**
 * @author yinchong
 * @create 2020/5/8 21:33
 * @description
 */
public class FeignLearn {
    public static void main(String[] args) {
        FeignTest();
    }

    private static void hystrixFeignTest() {
        GitHub gitHub = HystrixFeign.builder()
                .target(GitHub.class, "https://github.com/");
        gitHub.contributors("OpenFeign", "feign");
    }

    private static void ribbonFeignTest(){
        GitHub gitHub = Feign.builder()
                .client(RibbonClient.create())
                .target(GitHub.class, "https://github.com/");
        gitHub.contributors("OpenFeign", "feign");
    }

    private static void FeignTest() {
        GitHub gitHub = Feign.builder()
                .decoder(new GsonDecoder())
                .target(GitHub.class, "https://github.com/");

        List<Contributor> list = gitHub.contributors("OpenFeign", "feign");
        System.out.println(list);
    }
}
