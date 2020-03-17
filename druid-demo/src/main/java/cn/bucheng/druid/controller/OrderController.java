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

import cn.bucheng.druid.domain.Order;
import cn.bucheng.druid.service.OrderService;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author yinchong
 * @create 2020/3/17 14:56
 * @description
 */
@Controller()
@RequestMapping("order")
public class OrderController implements BeanNameAware {
    @Autowired
    private OrderService orderService;

    @RequestMapping("save")
    @ResponseBody
    public Object addOrder(@RequestBody Order order){
        orderService.addOrder(order);
        return "success";
    }

    @RequestMapping("errorList")
    public Object errorList(){
        return orderService.errorList();
    }

    @RequestMapping("listAll")
    @ResponseBody
    public Object listAll(){
        return orderService.listAll();
    }

    @Override
    public void setBeanName(String name) {
        System.out.println(name);
    }
}
