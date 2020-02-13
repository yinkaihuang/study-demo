package cn.bucheng.springmybatisdemo.datasource;
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

import cn.bucheng.springmybatisdemo.pluging.MyPageInterceptor;
import cn.bucheng.springmybatisdemo.pluging.MyResultInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yinchong
 * @create 2020/2/13 16:00
 * @description
 */
public class DynamicDataSourceRegister implements ImportBeanDefinitionRegistrar, PriorityOrdered {

    public static final String DATA_SOURCE_NAME = "dataSource";
    public static final String DATA_SOURCE = "dataSource";
    public static final String JDBC_TEMPLATE = "jdbcTemplate";
    public static final String SQL_SESSION = "sqlSession";
    public static final String TRANSACTION = "transaction";
    public static final String DATA_SOURCE_NAME_2 = "dataSource2";
    public static final String JDBC_TEMPLATE_2 = "jdbcTemplate2";
    public static final String SQL_SESSION_2 = "sqlSession2";
    public static final String TRANSACTION_2 = "transaction2";

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        /**创建第一个数据源**/
        //创建DataSource数据结构
        BeanDefinitionBuilder dataSourceBuilder = BeanDefinitionBuilder.genericBeanDefinition(DataSourceFactory.class);
        dataSourceBuilder.setLazyInit(false);
        dataSourceBuilder.addPropertyValue("url", "jdbc:mysql://localhost:3306/mybatis_test?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai");
        dataSourceBuilder.addPropertyValue("className", "com.mysql.cj.jdbc.Driver");
        dataSourceBuilder.addPropertyValue("username", "root");
        dataSourceBuilder.addPropertyValue("password", "123456");
        registry.registerBeanDefinition(DATA_SOURCE_NAME, dataSourceBuilder.getBeanDefinition());

        //创建JdbcTemplate数据结构
        BeanDefinitionBuilder jdbcBuilder = BeanDefinitionBuilder.genericBeanDefinition(JdbcTemplate.class);
        jdbcBuilder.setLazyInit(false);
        jdbcBuilder.addPropertyReference(DATA_SOURCE, DATA_SOURCE_NAME);
        registry.registerBeanDefinition(JDBC_TEMPLATE, jdbcBuilder.getBeanDefinition());

        //创建SqlSessionFactory数据结构
        BeanDefinitionBuilder sqlSessionBuilder = BeanDefinitionBuilder.genericBeanDefinition(SqlSessionFactoryBean.class);
        sqlSessionBuilder.setLazyInit(false);
        //如果没mapperLocations,则需要xml文件和java文件放在一起
        sqlSessionBuilder.addPropertyValue("plugins",new Interceptor[]{new MyPageInterceptor(),new MyResultInterceptor()});
        sqlSessionBuilder.addPropertyValue("typeAliasesPackage",new String[]{"cn.bucheng.springmybatisdemo.domain"});
        sqlSessionBuilder.addPropertyValue("mapperLocations",new String[]{"classpath:mapper/*.xml"});
        //
        sqlSessionBuilder.addPropertyReference(DATA_SOURCE, DATA_SOURCE_NAME);
        registry.registerBeanDefinition(SQL_SESSION, sqlSessionBuilder.getBeanDefinition());

        //创建DataSourceTransaction
        BeanDefinitionBuilder transactionBuilder = BeanDefinitionBuilder.genericBeanDefinition(DataSourceTransactionManager.class);
        transactionBuilder.setLazyInit(false);
        transactionBuilder.addPropertyReference(DATA_SOURCE, DATA_SOURCE_NAME);
        registry.registerBeanDefinition(TRANSACTION, transactionBuilder.getBeanDefinition());

        //构建mapperscan数据结构
        BeanDefinitionBuilder mapperBuilder = createMapperScan("cn.bucheng.springmybatisdemo.mapper", SQL_SESSION, new String[]{});
        registry.registerBeanDefinition("mapperScan", mapperBuilder.getBeanDefinition());

        /**创建第二个数据源**/
        //创建DataSource数据结构
        BeanDefinitionBuilder dataSourceBuilder2 = BeanDefinitionBuilder.genericBeanDefinition(DataSourceFactory.class);
        dataSourceBuilder2.setLazyInit(false);
        dataSourceBuilder2.addPropertyValue("url", "jdbc:mysql://localhost:3306/db0?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai");
        dataSourceBuilder2.addPropertyValue("className", "com.mysql.cj.jdbc.Driver");
        dataSourceBuilder2.addPropertyValue("username", "root");
        dataSourceBuilder2.addPropertyValue("password", "123456");
        registry.registerBeanDefinition(DATA_SOURCE_NAME_2, dataSourceBuilder2.getBeanDefinition());

        //创建JdbcTemplate数据结构
        BeanDefinitionBuilder jdbcBuilder2 = BeanDefinitionBuilder.genericBeanDefinition(JdbcTemplate.class);
        jdbcBuilder2.setLazyInit(false);
        jdbcBuilder2.addPropertyReference(DATA_SOURCE, DATA_SOURCE_NAME_2);
        registry.registerBeanDefinition(JDBC_TEMPLATE_2, jdbcBuilder2.getBeanDefinition());

        //创建SqlSessionFactory数据结构
        BeanDefinitionBuilder sqlSessionBuilder2 = BeanDefinitionBuilder.genericBeanDefinition(SqlSessionFactoryBean.class);
        sqlSessionBuilder2.setLazyInit(false);
        //如果没mapperLocations,则需要xml文件和java文件放在一起
        sqlSessionBuilder2.addPropertyValue("plugins",new Interceptor[]{new MyPageInterceptor(),new MyResultInterceptor()});
        sqlSessionBuilder2.addPropertyValue("typeAliasesPackage",new String[]{"cn.bucheng.springmybatisdemo.entity"});
        sqlSessionBuilder2.addPropertyValue("mapperLocations",new String[]{"classpath:mapper2/*.xml"});
        //
        sqlSessionBuilder2.addPropertyReference(DATA_SOURCE, DATA_SOURCE_NAME_2);
        registry.registerBeanDefinition(SQL_SESSION_2, sqlSessionBuilder2.getBeanDefinition());

        //创建DataSourceTransaction
        BeanDefinitionBuilder transactionBuilder2 = BeanDefinitionBuilder.genericBeanDefinition(DataSourceTransactionManager.class);
        transactionBuilder2.setLazyInit(false);
        transactionBuilder2.addPropertyReference(DATA_SOURCE, DATA_SOURCE_NAME_2);
        registry.registerBeanDefinition(TRANSACTION_2, transactionBuilder2.getBeanDefinition());

        //构建mapperscan数据结构
        BeanDefinitionBuilder mapperBuilder2 = createMapperScan("cn.bucheng.springmybatisdemo.dao", SQL_SESSION_2, new String[]{});
        registry.registerBeanDefinition("mapperScan2", mapperBuilder2.getBeanDefinition());
    }


    /**
     * 构建MapperScan数据结构模拟，mybatis扫描包动作
     *
     * @param value
     * @param sqlSessionFactoryRef basePackage
     * @return
     */
    BeanDefinitionBuilder createMapperScan(String value, String sqlSessionFactoryRef, String[] basePackage) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(MapperScannerConfigurer.class);
        builder.addPropertyValue("processPropertyPlaceHolders", true);
        List<String> basePackages = new ArrayList<>();
        //添加value
        basePackages.addAll(
                Arrays.stream(new String[]{value}).filter(StringUtils::hasText).collect(Collectors.toList()));
        basePackages.addAll(Arrays.stream(basePackage).filter(StringUtils::hasText)
                .collect(Collectors.toList()));
        basePackages.addAll(Arrays.stream(new String[]{}).map(ClassUtils::getPackageName)
                .collect(Collectors.toList()));
        //添加sqlSessionFactoryRef
        if (StringUtils.hasText(sqlSessionFactoryRef)) {
            builder.addPropertyValue("sqlSessionFactoryBeanName", sqlSessionFactoryRef);
        }
        builder.addPropertyValue("basePackage", StringUtils.collectionToCommaDelimitedString(basePackages));
        return builder;
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }
}
