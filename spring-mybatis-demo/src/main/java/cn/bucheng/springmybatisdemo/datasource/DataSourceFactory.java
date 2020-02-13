package cn.bucheng.springmybatisdemo.datasource;
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

import org.springframework.beans.factory.FactoryBean;
import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;

/**
 * @author yinchong
 * @create 2020/2/13 16:01
 * @description
 */
public class DataSourceFactory implements FactoryBean<DataSource> {
    private String url;
    private String className;
    private String username;
    private String password;


    @Override
    public DataSource getObject() throws Exception {
        DataSourceBuilder builder = DataSourceBuilder.create();
        builder.url(url);
        builder.driverClassName(className);
        builder.username(username);
        builder.password(password);
        return builder.build();
    }

    @Override
    public Class<?> getObjectType() {
        return DataSource.class;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
