package cn.bucheng.mybatis.plugins;
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

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

/**
 * @author yinchong
 * @create 2020/2/11 16:43
 * @description
 */
@Intercepts({@Signature(
        type = StatementHandler.class,//对象类型，只能是四大对象类型
        method = "prepare", //拦截方法
        args = {Connection.class, Integer.class/*方法中用到的参数类型*/})})//可以点击拦截对象查看拦截方法的参数，
public class PreparementPluging implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        if (statementHandler instanceof PreparedStatement) {
            PreparedStatement ps = (PreparedStatement) statementHandler;
            ps.setFetchSize(Integer.MIN_VALUE);
            ps.setFetchDirection(ResultSet.FETCH_REVERSE);
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
