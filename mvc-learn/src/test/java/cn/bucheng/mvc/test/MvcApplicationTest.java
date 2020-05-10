package cn.bucheng.mvc.test;
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

import cn.bucheng.mvc.learn.MvcApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * @author yinchong
 * @create 2020/5/10 12:31
 * @description
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MvcApplication.class)
@WebAppConfiguration
public class MvcApplicationTest {
    @Autowired
    private BeanFactory beanFactory;

    @Test
    public void test(){
        cn.bucheng.mvc.learn.bean.Test bean = beanFactory.getBean(cn.bucheng.mvc.learn.bean.Test.class);
        bean.test();
    }
}
