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

import cn.bucheng.springmybatisdemo.annotation.EnableMuchDataSource;
import cn.bucheng.springmybatisdemo.pluging.MyPageInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.logging.log4j.util.Strings;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
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
public class DynamicDataSourceRegister implements ImportBeanDefinitionRegistrar, EnvironmentAware, PriorityOrdered {

    public static final String DATA_SOURCE_NAME = "dataSource";
    public static final String DATA_SOURCE = "dataSource";
    public static final String JDBC_TEMPLATE = "jdbcTemplate";
    public static final String SQL_SESSION = "sqlSession";
    public static final String TRANSACTION = "transaction";
    public static final String MAPPER_SCAN = "mapperScan";

    private Environment environment;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes muchSourceAttr = AnnotationAttributes
                .fromMap(metadata.getAnnotationAttributes(EnableMuchDataSource.class.getName()));
        if (muchSourceAttr == null) {
            return;
        }

        String prefix = muchSourceAttr.getString("prefix");
        String commonConfigLocation = muchSourceAttr.getString("configLocation");
        AnnotationAttributes[] values = (AnnotationAttributes[]) muchSourceAttr.get("value");
        for (AnnotationAttributes value : values) {
            String dbPrefix = value.getString("dbPrefix");
            String[] locations = value.getStringArray("mapperLocations");
            String aliasesPackage = value.getString("typeAliasesPackage");
            String[] mapperScanPackages = value.getStringArray("mapperScanPackages");
            String configLocation = value.getString("configLocation");
            String finalConfigLocation = configLocation;
            if (Strings.isBlank(finalConfigLocation)) {
                finalConfigLocation = commonConfigLocation;
            }
            createAndRegisterMapperBean(registry, prefix, dbPrefix, locations, aliasesPackage, mapperScanPackages,finalConfigLocation);
        }
    }

    private void createAndRegisterMapperBean(BeanDefinitionRegistry registry, String prefix, String dbPrefix, String[] locations, String aliasesPackage, String[] mapperScanPackages,String configLocation) {
        /**创建第一个数据源**/
        //创建DataSource数据结构
        BeanDefinitionBuilder dataSourceBuilder = BeanDefinitionBuilder.genericBeanDefinition(DataSourceFactory.class);
        dataSourceBuilder.setLazyInit(false);
        dataSourceBuilder.addPropertyValue("dbPrefix", prefix + "." + dbPrefix);
        dataSourceBuilder.addPropertyValue("environment", environment);
        registry.registerBeanDefinition(dbPrefix + DATA_SOURCE_NAME, dataSourceBuilder.getBeanDefinition());

        //创建JdbcTemplate数据结构
        BeanDefinitionBuilder jdbcBuilder = BeanDefinitionBuilder.genericBeanDefinition(JdbcTemplate.class);
        jdbcBuilder.setLazyInit(false);
        jdbcBuilder.addPropertyReference(DATA_SOURCE, dbPrefix + DATA_SOURCE_NAME);
        registry.registerBeanDefinition(dbPrefix + JDBC_TEMPLATE, jdbcBuilder.getBeanDefinition());

        //创建SqlSessionFactory数据结构
        BeanDefinitionBuilder sqlSessionBuilder = BeanDefinitionBuilder.genericBeanDefinition(SqlSessionFactoryBean.class);
        sqlSessionBuilder.setLazyInit(false);
        //如果没mapperLocations,则需要xml文件和java文件放在一起
        sqlSessionBuilder.addPropertyValue("plugins", new Interceptor[]{new MyPageInterceptor()});
        if (!Strings.isBlank(aliasesPackage)) {
            sqlSessionBuilder.addPropertyValue("typeAliasesPackage", aliasesPackage);
        }

        if(!Strings.isBlank(configLocation)){
            ResourcePatternResolver resolver = (ResourcePatternResolver) new PathMatchingResourcePatternResolver();
            Resource resource = resolver.getResource(configLocation);
            sqlSessionBuilder.addPropertyValue("configLocation",resource);
        }

        if (locations != null && locations.length > 0) {
            sqlSessionBuilder.addPropertyValue("mapperLocations", locations);
        }
        //
        sqlSessionBuilder.addPropertyReference(DATA_SOURCE, dbPrefix + DATA_SOURCE_NAME);
        registry.registerBeanDefinition(dbPrefix + SQL_SESSION, sqlSessionBuilder.getBeanDefinition());

        //创建DataSourceTransaction
        BeanDefinitionBuilder transactionBuilder = BeanDefinitionBuilder.genericBeanDefinition(DataSourceTransactionManager.class);
        transactionBuilder.setLazyInit(false);
        transactionBuilder.addPropertyReference(DATA_SOURCE, dbPrefix + DATA_SOURCE_NAME);
        registry.registerBeanDefinition(dbPrefix + TRANSACTION, transactionBuilder.getBeanDefinition());

        //构建mapperscan数据结构
        BeanDefinitionBuilder mapperBuilder = createMapperScan(mapperScanPackages, dbPrefix + SQL_SESSION, new String[]{});
        registry.registerBeanDefinition(dbPrefix + MAPPER_SCAN, mapperBuilder.getBeanDefinition());
    }


    /**
     * 构建MapperScan数据结构模拟，mybatis扫描包动作
     *
     * @param values
     * @param sqlSessionFactoryRef basePackage
     * @return
     */
    BeanDefinitionBuilder createMapperScan(String[] values, String sqlSessionFactoryRef, String[] basePackage) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(MapperScannerConfigurer.class);
        builder.addPropertyValue("processPropertyPlaceHolders", true);
        List<String> basePackages = new ArrayList<>();
        //添加value
        basePackages.addAll(
                Arrays.stream(values).filter(StringUtils::hasText).collect(Collectors.toList()));
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

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
