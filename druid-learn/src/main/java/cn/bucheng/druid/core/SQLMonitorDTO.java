package cn.bucheng.druid.core;
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

import lombok.Data;

/**
 * @author yinchong
 * @create 2020/3/19 10:23
 * @description
 */
@Data
public class SQLMonitorDTO {
    //应用名称
    private String App;
    private Integer BatchSizeMax;
    private Integer BatchSizeTotal;
    private Integer BlobOpenCount;
    private Integer ClobOpenCount;
    //最大并发数量
    private Integer ConcurrentMax;
    //数据库类型
    private String DbType;
    //更新行数
    private Integer EffectedRowCount;
    private Integer EffectedRowCountMax;
    //错误数量
    private Integer ErrorCount;
    //执行次数
    private Integer ExecuteCount;
    //读取行数
    private Integer FetchRowCount;
    private Integer FetchRowCountMax;
    private Integer InTransactionCount;
    //最大耗时
    private Integer MaxTimespan;
    //执行SQL语句
    private String SQL;
    //总共耗时
    private Integer TotalTime;
}
