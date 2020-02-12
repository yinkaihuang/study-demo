package cn.bucheng.springmybatisdemo.configuration;
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

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author yinchong
 * @create 2020/2/12 10:20
 * @description
 */
@Slf4j
@ConfigurationProperties(prefix = "spring.datasource")
public class MyDataSourceProperties extends DataSourceProperties {

    @Override
    public void setUrl(String url) {
        if (!url.contains("serverTimezone")) {
            String timeZone = "serverTimezone=Asia/Shanghai";
            log.info("no find serverTimezone in url,set default timeZone:{}", timeZone);
            if (url.contains("?")) {
                url += "&" + timeZone;
            } else {
                url += "?" + timeZone;
            }
        }
        super.setUrl(url);
    }
}
