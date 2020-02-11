package cn.bucheng.mybatis.code;
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

import cn.bucheng.mybatis.mapper.UserMapper;
import cn.bucheng.mybatis.plugins.PageIntercepts;
import cn.bucheng.mybatis.plugins.ResultIntercepts;
import com.github.pagehelper.PageHelper;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.junit.Test;

import javax.sql.DataSource;
import java.util.List;
import java.util.Properties;

/**
 * @author yinchong
 * @create 2020/2/11 20:23
 * @description
 */
public class UserMapperTest {

    @Test
    public void test(){
        //创建连接池
        DataSource dataSource = new PooledDataSource("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1:3306/mybatis_test", "root", "123456");
        //事务
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        //创建环境
        Environment environment = new Environment("development", transactionFactory, dataSource);
        //创建配置
        Configuration configuration = new Configuration(environment);
        //开启驼峰规则
        configuration.setMapUnderscoreToCamelCase(true);
        //加入资源（Mapper接口） ,这里xml文件必须和当前接口在同一个目录下面
        //因为在源码中写死了读同一路径xml loadXmlResource()
        //String xmlResource = type.getName().replace('.','/')+".xml";
        configuration.addMapper(UserMapper.class);

        //添加插件
        configuration.addInterceptor(new PageIntercepts());
        configuration.addInterceptor(new ResultIntercepts());
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        properties.setProperty("dialect","mysql");
        properties.setProperty("offsetAsPageNum","true");
        properties.setProperty("rowBoundsWithCount","true");
        properties.setProperty("pageSizeZero","true");
        properties.setProperty("reasonable","false");
        properties.setProperty("params","pageNum=pageHelperStart;pageSize=pageHelperRows;");
        properties.setProperty("supportMethodsArguments","false");
        properties.setProperty("returnPageInfo","none");
        pageHelper.setProperties(properties);
        configuration.addInterceptor(pageHelper);

        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
        SqlSession session = sqlSessionFactory.openSession();
        try {
            //statement:sql唯一标识(mapper.xml映射文件中的id标识)
            List<Object> objects = session.selectList("cn.bucheng.mybatis.mapper.UserMapper.findLimit");
            System.out.println(objects);
            List<Object> userList = session.selectList("cn.bucheng.mybatis.mapper.UserMapper.findAll");
            System.out.println(userList);
            //操作数据时，需要有提交操作
            session.commit();
        } finally {
            session.close();
        }

    }
}
