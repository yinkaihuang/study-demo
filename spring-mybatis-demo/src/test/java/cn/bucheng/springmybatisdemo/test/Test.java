package cn.bucheng.springmybatisdemo.test;
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

import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.util.Date;

/**
 * @author yinchong
 * @create 2020/2/19 20:13N
 * @description
 */
public class Test {

    public static void main(String[] args) {
        IDCard idCard = IDCard.builder().date(new Date()).idCard("test_123").build();
        Student student = Student.builder().gender("男").name("银从").idCard(idCard).build();
        MetaObject metaObject = SystemMetaObject.forObject(student);
        metaObject.setValue("idCard.idCard","hello_123");
        System.out.println(idCard);
    }
}
