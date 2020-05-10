package cn.bucheng.druid.mapper;
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
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author yinchong
 * @create 2020/3/17 14:48
 * @description
 */
public interface OrderMapper {
    List<Order> listAll();

    List<Order> errorList();

    int save(Order order);

    int updateById(Order order);

    Order findById(@Param("id") Long id);
}
