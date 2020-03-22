package cn.bucheng.druid.core.test;
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

import java.util.Date;

/**
 * @author yinchong
 * @create 2020/3/19 14:41
 * @description
 */
@Data
public class LogInfo {
    private String sql;
    private String sqlParam;
    private Long executeMillisMax;
    private Integer maxParallel;
    private Long executeMillisTotal;
    private Long executeCount;
    private Long executeErrorCount;
    private Date maxOccurTime;
    private Long his_0_1;
    private Long his_1_10;
    private Integer his_10_100;
    private Integer his_100_1000;
    private Integer his_1000_10000;
    private Integer his_10000_100000;
    private Integer his_100000_1000000;
    private Integer his_1000000_more;
    private String lastErrorClass;
    private String lastErrorMessage;
    private String lastErrorStackTrace;
    private Date lastErrorTime;
}
