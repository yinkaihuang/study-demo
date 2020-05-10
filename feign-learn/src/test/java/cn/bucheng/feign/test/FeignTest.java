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
import com.netflix.client.ClientFactory;
import com.netflix.client.config.IClientConfig;
import com.netflix.config.ConfigurationManager;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.RoundRobinRule;
import com.netflix.loadbalancer.ZoneAwareLoadBalancer;
import feign.*;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.hystrix.FallbackFactory;
import feign.hystrix.HystrixFeign;
import feign.hystrix.SetterFactory;
import feign.ribbon.LBClient;
import feign.ribbon.LBClientFactory;
import feign.ribbon.RibbonClient;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author yinchong
 * @create 2020/5/10 10:10
 * @description
 */
public class FeignTest {

    /**
     * 普通请求未设置超时时间
     */
    @Test
    public void normalFeignTest() {
        FeignService feignService = Feign.builder()
                .decoder(new GsonDecoder())
                .target(FeignService.class, "http://127.0.0.1:8080");
        Map<String, Object> map = feignService.test();
        System.out.println(map);
    }

    /**
     * 普通feign测试代码，通过设置http请求参数时间
     */
    @Test
    public void normalFeignTimeOutTest() {
        Request.Options options = new Request.Options(5, TimeUnit.SECONDS, 6, TimeUnit.SECONDS, true);
        FeignService feignService = Feign.builder()
                .options(options)
                .encoder(new GsonEncoder())
                .retryer(new MyRetryer())
                .decoder(new GsonDecoder())
                .target(FeignService.class, "http://127.0.0.1:8080");

        Map<String, Object> map = feignService.timeout(4);
        System.out.println(map);
    }

    static class MyRetryer implements Retryer {

        @Override
        public void continueOrPropagate(RetryableException e) {
            throw e;
        }

        @Override
        public Retryer clone() {
            return this;
        }
    }

    /**
     * hystrix测试代码，并设置超时参数
     */
    @Test
    public void hystrixFeignTest() {
        FeignService feignService = HystrixFeign.builder()
                .decoder(new GsonDecoder())
                .retryer(new MyRetryer())
                .setterFactory(new MySetterFactory())
                .decoder(new GsonDecoder())
                .encoder(new GsonEncoder())
                .target(FeignService.class, "http://127.0.0.1:8080", new MyFallBack());
        System.out.println(feignService.timeout(7));
    }

    static class MyFallBack implements FallbackFactory<FeignService> {

        @Override
        public FeignService create(Throwable cause) {
            return new FeignService() {
                @Override
                public Map<String, Object> test() {
                    return error();
                }

                @Override
                public Map<String, Object> timeout(Integer second) {
                    return error();
                }

                private Map<String, Object> error() {
                    Map<String, Object> result = new HashMap<>();
                    result.put("code", 500);
                    result.put("msg", "fallback invoke");
                    return result;
                }
            };
        }
    }

    static class MySetterFactory implements SetterFactory {

        @Override
        public HystrixCommand.Setter create(Target<?> target, Method method) {
            String groupKey = target.name();
            String commandKey = Feign.configKey(target.type(), method);
            return HystrixCommand.Setter
                    .withGroupKey(HystrixCommandGroupKey.Factory.asKey(groupKey))
                    .andCommandKey(HystrixCommandKey.Factory.asKey(commandKey))
                    .andCommandPropertiesDefaults(HystrixCommandProperties.defaultSetter().withExecutionTimeoutInMilliseconds(6000));
        }
    }

    /**
     * ribbon使用
     * @throws IOException
     */
    @Test
    public void ribbonFeignTest() throws IOException {
        ConfigurationManager.loadPropertiesFromResources("sample-client.properties");
        RibbonClient client = RibbonClient.builder().lbClientFactory(new LBClientFactory() {

            @Override
            public LBClient create(String clientName) {
                IClientConfig config = ClientFactory.getNamedConfig(clientName);
                ILoadBalancer lb = ClientFactory.getNamedLoadBalancer(clientName);
                ZoneAwareLoadBalancer zb = (ZoneAwareLoadBalancer) lb;
                zb.setRule(new RoundRobinRule());
                return LBClient.create(lb, config);
            }
        }).build();
        FeignService feignService = Feign.builder()
                .decoder(new GsonDecoder())
                .encoder(new GsonEncoder())
                .client(client)
                .target(FeignService.class, "http://sample-client");

        for (int i = 0; i < 10; i++) {
            try {
                Map<String, Object> result = feignService.test();
                System.out.println(result);
            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }
}
