package cn.bucheng.simple.test;
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

import cn.bucheng.simple.learn.ConfigurationUtils;
import org.junit.Test;

import java.io.IOException;

/**
 * @author yinchong
 * @create 2020/5/10 12:16
 * @description
 */
public class ConfigurationTest {


    @Test
    public void loadSimpleResourceTest() throws IOException {
        ConfigurationUtils.Configuration configuration = ConfigurationUtils.loadPropertiesFromResources("test.properties");
        System.out.println(configuration.getProperty("name"));
    }

    @Test
    public void loadMetaInfResourceTest() throws IOException {
        ConfigurationUtils.Configuration configuration = ConfigurationUtils.loadPropertiesFromResources("META-INF/test2.properties");
        System.out.println(configuration.getProperty("name"));
    }
}
