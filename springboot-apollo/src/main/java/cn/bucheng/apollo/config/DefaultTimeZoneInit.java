package cn.bucheng.apollo.config;
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

import com.ctrip.framework.apollo.internals.DefaultConfig;
import org.apache.logging.log4j.util.Strings;
import org.springframework.boot.origin.OriginTrackedValue;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.Ordered;
import org.springframework.core.env.*;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author yinchong
 * @create 2020/2/12 13:03
 * @description
 */
@Component
public class DefaultTimeZoneInit implements EnvironmentAware, Ordered {


    public static final String CONFIGURATION_PROPERTIES = "configurationProperties";
    public static final String JDBC = "jdbc";
    public static final String SERVER_TIME_ZONE = "serverTimezone=";
    public static final String ASIA_SHANGHAI = "Asia/Shanghai";

    @Override
    public void setEnvironment(Environment env) {
        ConfigurableEnvironment environment = (ConfigurableEnvironment) env;
//        String property = environment.getProperty("spring.datasource.url");
//        System.out.println(property);
        List<ReplaceEnvMap> replaceList = new LinkedList<>();
        MutablePropertySources propertySources = environment.getPropertySources();
        Iterator<PropertySource<?>> iterator = propertySources.iterator();
        String beforeName = "";
        while (iterator.hasNext()) {
            PropertySource<?> next = iterator.next();
            String name = next.getName();
            if (name.equals(CONFIGURATION_PROPERTIES)) {
                beforeName = name;
                continue;
            }
            if(name.equals("ApolloPropertySources")){
                CompositePropertySource compositeSource = (CompositePropertySource) next;
                Collection<PropertySource<?>> sources = compositeSource.getPropertySources();
                for(PropertySource source:sources){
                   DefaultConfig config = (DefaultConfig) source.getSource();
//                  String properties =  config.("m_configProperties",null);
                }
            }
            Object source = next.getSource();
            if (!(source instanceof Map)) {
                beforeName = name;
                continue;
            }
            Map<String, Object> sourceMap = (Map<String, Object>) source;
            Map<String, Object> replaceMap = new LinkedHashMap<>();
            if (sourceMap == null || sourceMap.size() == 0) {
                beforeName = name;
                continue;
            }
            boolean replace = false;
            for (Map.Entry<String, Object> entry : sourceMap.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (value instanceof OriginTrackedValue) {
                    OriginTrackedValue temp = (OriginTrackedValue) value;
                    replaceMap.put(key, temp.getValue());
                } else {
                    replaceMap.put(key, value);
                }
                if (!key.endsWith("url")) {
                    continue;
                }
                String valueStr = String.valueOf(value);
                if (!valueStr.toLowerCase().trim().startsWith(JDBC)) {
                    continue;
                }
                if (valueStr.contains(SERVER_TIME_ZONE)) {
                    continue;
                }
                valueStr = appendServerTimeZone(valueStr);
                replace = true;
                replaceMap.put(key, valueStr);
            }
            if (replace) {
                createReplaceEntity(replaceList, beforeName, name, replaceMap);
            }
            beforeName = name;
        }

        for (ReplaceEnvMap envMap : replaceList) {
            handleReplaceList(propertySources, envMap);
        }
    }

    /**
     * 处理需要替换的source
     *
     * @param propertySources
     * @param envMap
     */
    private void handleReplaceList(MutablePropertySources propertySources, ReplaceEnvMap envMap) {
        propertySources.remove(envMap.name);
        MapPropertySource tempSource = new MapPropertySource(envMap.name, envMap.replaceMap);
        if (Strings.isBlank(envMap.beforeName)) {
            propertySources.addFirst(tempSource);
        } else {
            propertySources.addAfter(envMap.beforeName, tempSource);
        }
    }

    //创建替换数据结构
    private void createReplaceEntity(List<ReplaceEnvMap> replaceList, String beforeName, String name, Map<String, Object> replaceMap) {
        ReplaceEnvMap replaceEnvMap = new ReplaceEnvMap();
        replaceEnvMap.beforeName = beforeName;
        replaceEnvMap.name = name;
        replaceEnvMap.replaceMap = replaceMap;
        replaceList.add(replaceEnvMap);
    }

    //添加时间字段
    private String appendServerTimeZone(String valueStr) {
        if (valueStr.contains("?")) {
            valueStr += "&" + SERVER_TIME_ZONE + ASIA_SHANGHAI;
        } else {
            valueStr += "?" + SERVER_TIME_ZONE + ASIA_SHANGHAI;
        }
        return valueStr;
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }

    class ReplaceEnvMap {
        private String beforeName;
        private String name;
        private Map<String, Object> replaceMap;
    }
}
