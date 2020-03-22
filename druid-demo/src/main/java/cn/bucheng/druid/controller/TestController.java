package cn.bucheng.druid.controller;
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

import cn.bucheng.druid.core.DruidMonitorService;
import com.alibaba.druid.stat.DruidStatService;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yinchong
 * @create 2020/3/17 15:23
 * @description
 */
@RestController()
@RequestMapping("test")
public class TestController implements BeanNameAware {


    public static final String SQL_QUERY_DETAIL = "/sql.json?orderBy=SQL&orderType=desc&page=1&perPageCount=1000000&";
    public static final String REST_ALL = "/reset-all.json";
    public static final String WALL_JSON = "/wall.json";

    @RequestMapping("hello")
    public Object hello() {
        return "hello";
    }

    @Override
    public void setBeanName(String name) {
        System.out.println(name);
    }

    @RequestMapping("show")
    public Object show() {
        String service = DruidStatService.getInstance().service(SQL_QUERY_DETAIL);
        return service;
    }

    @RequestMapping("rest")
    public Object reset() {
        return DruidStatService.getInstance().service(REST_ALL);
    }


    @RequestMapping("wall")
    public Object wall(){
        return DruidStatService.getInstance().service(WALL_JSON);
    }


    @RequestMapping("reportAndClear")
    public Object reportAndClear(){
        return DruidMonitorService.getInstance().reportMonitor();
    }
}
