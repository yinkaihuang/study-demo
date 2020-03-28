package cn.bucheng.springmybatisdemo.controller;
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

import cn.bucheng.springmybatisdemo.dao.UserDao;
import cn.bucheng.springmybatisdemo.domain.User;
import cn.bucheng.springmybatisdemo.entity.UserEntity;
import cn.bucheng.springmybatisdemo.mapper.UserMapper;
import com.alibaba.druid.stat.DruidStatService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yinchong
 * @create 2020/3/23 10:35
 * @description
 */
@RestController
@RequestMapping("test")
public class TestController {
    public static final String SQL_QUERY_DETAIL = "/sql.json";
    public static final String REST_ALL = "/reset-all.json";
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserDao userDao;

    @RequestMapping("listUser")
    public Object listUser() {
        PageHelper.startPage(1, 20);
        Page<User> users = userMapper.pageAll();
        return users;
    }

    @RequestMapping("listUser2")
    public Object listUser2() {
        PageHelper.startPage(1, 20);
        Page<UserEntity> userRecord = userDao.pageAll();
        return userRecord;
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

}
