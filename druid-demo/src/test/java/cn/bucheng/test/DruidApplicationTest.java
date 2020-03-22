package cn.bucheng.test;
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

import cn.bucheng.druid.DruidApplication;
import cn.bucheng.druid.domain.Order;
import cn.bucheng.druid.mapper.OrderMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author yinchong
 * @create 2020/3/20 8:02
 * @description
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DruidApplication.class,webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class DruidApplicationTest {

    @Autowired
    private OrderMapper orderMapper;


    @Test
    public void testList(){
        long startTime = System.currentTimeMillis();
        for(int i=0;i<1000;i++){
            orderMapper.listAll();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("use time:"+(endTime-startTime));
    }


    @Test
    public void testSave(){
        long startTime = System.currentTimeMillis();
        for(int i=0;i<10000;i++){
            Order.OrderBuilder orderBuilder = Order.builder().detail("this is test").id((long) (i*3 + 100000) ).orderId((long) i);
            orderMapper.save(orderBuilder.build());
        }
        long endTime = System.currentTimeMillis();
        System.out.println("use time:"+(endTime-startTime));
    }
}
