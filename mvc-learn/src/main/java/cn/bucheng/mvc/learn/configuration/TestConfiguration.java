package cn.bucheng.mvc.learn.configuration;
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

import cn.bucheng.mvc.learn.bean.OneTest;
import cn.bucheng.mvc.learn.bean.Test;
import cn.bucheng.mvc.learn.bean.ThreeTest;
import cn.bucheng.mvc.learn.bean.TwoTest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yinchong
 * @create 2020/5/10 12:34
 * @description
 */
@Configuration
public class TestConfiguration {

    @Configuration
    protected  static class TestOneConfiguration{

        @Bean
        @ConditionalOnMissingBean
        public Test oneTest(){
            return new OneTest();
        }

        @Configuration
        protected static class TestTwoConfiguration{

            @Bean
            @ConditionalOnMissingBean
            public Test twoTest(){
                return new TwoTest();
            }

            protected static class TestThreeConfiguration{

                @Bean
                @ConditionalOnMissingBean
                public Test threeTes(){
                    return new ThreeTest();
                }
            }
        }

    }


}
