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
import cn.bucheng.springmybatisdemo.pluging.MyResultInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.logging.log4j.util.Strings;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.spring.mapper.ClassPathMapperScanner;
import tk.mybatis.spring.mapper.MapperFactoryBean;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yinchong
 * @create 2020/2/17 19:49
 * @description
 */
public class TkMybatisDataSourceRegister implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware {
    public static final String DATA_SOURCE_NAME = "dataSource";
    public static final String DATA_SOURCE = "dataSource";
    public static final String JDBC_TEMPLATE = "jdbcTemplate";
    public static final String SQL_SESSION_FACTORY = "sqlSessionFactory";
    public static final String TRANSACTION = "transaction";
    public static final String SQL_SESSION_TEMPLATE = "sqlSessionTemplate";

    private Environment environment;
    private ResourceLoader resourceLoader;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes muchSourceAttr = AnnotationAttributes
                .fromMap(metadata.getAnnotationAttributes(EnableMuchDataSource.class.getName()));
        if (muchSourceAttr == null) {
            return;
        }


        String prefix = muchSourceAttr.getString("prefix");
        AnnotationAttributes[] values = (AnnotationAttributes[]) muchSourceAttr.get("value");
        for (AnnotationAttributes value : values) {
            String dbPrefix = value.getString("dbPrefix");
            String[] locations = value.getStringArray("mapperLocations");
            String aliasesPackage = value.getString("typeAliasesPackage");
            createAndRegisterMapperBean(registry, prefix, dbPrefix, locations, aliasesPackage, value);
        }
    }


    private void createAndRegisterMapperBean(BeanDefinitionRegistry registry, String prefix, String dbPrefix,String[] locations,String aliasesPackage, AnnotationAttributes value) {
        //创建DataSource数据结构
        createAndRegisterDataSource(registry, prefix, dbPrefix);

        //创建JdbcTemplate数据结构
        createAndRegisterJdbcTemplate(registry, dbPrefix);

        //创建SqlSessionFactory数据结构
        createAndRegisterSqlSessionFactory(registry, dbPrefix,locations, aliasesPackage);

        //创建SqlSession
        createAndRegisterSqlSessionTemplate(registry, dbPrefix);

        //创建DataSourceTransaction
        createAndRegisterTransactionManager(registry, dbPrefix);

        //构建mapperscan数据结构
        buildTkScanner(registry, dbPrefix, value);
    }

    private void createAndRegisterTransactionManager(BeanDefinitionRegistry registry, String dbPrefix) {
        BeanDefinitionBuilder transactionBuilder = BeanDefinitionBuilder.genericBeanDefinition(DataSourceTransactionManager.class);
        transactionBuilder.setLazyInit(false);
        transactionBuilder.addPropertyReference(DATA_SOURCE, dbPrefix + DATA_SOURCE_NAME);
        registry.registerBeanDefinition(dbPrefix + TRANSACTION, transactionBuilder.getBeanDefinition());
    }

    private void createAndRegisterSqlSessionTemplate(BeanDefinitionRegistry registry, String dbPrefix) {
        BeanDefinitionBuilder sqlSessionTemplateBuilder = BeanDefinitionBuilder.genericBeanDefinition(SqlSessionTemplate.class);
        sqlSessionTemplateBuilder.addConstructorArgReference( dbPrefix + SQL_SESSION_FACTORY);
        registry.registerBeanDefinition(dbPrefix + SQL_SESSION_TEMPLATE, sqlSessionTemplateBuilder.getBeanDefinition());
    }

    private void createAndRegisterSqlSessionFactory(BeanDefinitionRegistry registry, String dbPrefix, String[] locations, String aliasesPackage) {
        BeanDefinitionBuilder sqlSessionBuilder = BeanDefinitionBuilder.genericBeanDefinition(SqlSessionFactoryBean.class);
        sqlSessionBuilder.setLazyInit(false);
        //如果没mapperLocations,则需要xml文件和java文件放在一起
        sqlSessionBuilder.addPropertyValue("plugins", new Interceptor[]{new MyPageInterceptor(), new MyResultInterceptor()});
        if (!Strings.isBlank(aliasesPackage)) {
            sqlSessionBuilder.addPropertyValue("typeAliasesPackage", aliasesPackage);
        }

//        if (!Strings.isBlank(configLocation)) {
//            ResourcePatternResolver resolver = (ResourcePatternResolver) new PathMatchingResourcePatternResolver();
//            Resource resource = resolver.getResource(configLocation);
//            sqlSessionBuilder.addPropertyValue("configLocation", resource);
//        }

        if (locations != null && locations.length > 0) {
            sqlSessionBuilder.addPropertyValue("mapperLocations", locations);
        }
        sqlSessionBuilder.addPropertyReference(DATA_SOURCE, dbPrefix + DATA_SOURCE_NAME);
        registry.registerBeanDefinition(dbPrefix + SQL_SESSION_FACTORY, sqlSessionBuilder.getBeanDefinition());
    }

    private void createAndRegisterJdbcTemplate(BeanDefinitionRegistry registry, String dbPrefix) {
        BeanDefinitionBuilder jdbcBuilder = BeanDefinitionBuilder.genericBeanDefinition(JdbcTemplate.class);
        jdbcBuilder.setLazyInit(false);
        jdbcBuilder.addPropertyReference(DATA_SOURCE, dbPrefix + DATA_SOURCE_NAME);
        registry.registerBeanDefinition(dbPrefix + JDBC_TEMPLATE, jdbcBuilder.getBeanDefinition());
    }

    private void createAndRegisterDataSource(BeanDefinitionRegistry registry, String prefix, String dbPrefix) {
        BeanDefinitionBuilder dataSourceBuilder = BeanDefinitionBuilder.genericBeanDefinition(DataSourceFactory.class);
        dataSourceBuilder.setLazyInit(false);
        dataSourceBuilder.addPropertyValue("dbPrefix", prefix + "." + dbPrefix);
        dataSourceBuilder.addPropertyValue("environment", environment);
        registry.registerBeanDefinition(dbPrefix + DATA_SOURCE_NAME, dataSourceBuilder.getBeanDefinition());
    }

    private void buildTkScanner(BeanDefinitionRegistry registry, String dbPrefix, AnnotationAttributes value) {
        ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry);
        // this check is needed in Spring 3.1
        if (resourceLoader != null) {
            scanner.setResourceLoader(resourceLoader);
        }

        Class<? extends Annotation> annotationClass = value.getClass("annotationClass");
        if (!Annotation.class.equals(annotationClass)) {
            scanner.setAnnotationClass(annotationClass);
        }

        Class<?> markerInterface = value.getClass("markerInterface");
        if (!Class.class.equals(markerInterface)) {
            scanner.setMarkerInterface(markerInterface);
        }

        Class<? extends BeanNameGenerator> generatorClass = value.getClass("nameGenerator");
        if (!BeanNameGenerator.class.equals(generatorClass)) {
            scanner.setBeanNameGenerator(BeanUtils.instantiateClass(generatorClass));
        }

        Class<? extends MapperFactoryBean> mapperFactoryBeanClass = value.getClass("factoryBean");
        if (!MapperFactoryBean.class.equals(mapperFactoryBeanClass)) {
            scanner.setMapperFactoryBean(BeanUtils.instantiateClass(mapperFactoryBeanClass));
        }

        scanner.setSqlSessionTemplateBeanName(dbPrefix + SQL_SESSION_TEMPLATE);
        scanner.setSqlSessionFactoryBeanName(dbPrefix + SQL_SESSION_FACTORY);

        List<String> basePackages = new ArrayList<String>();
        for (String pkg : value.getStringArray("value")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        for (String pkg : value.getStringArray("basePackages")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        for (Class<?> clazz : value.getClassArray("basePackageClasses")) {
            basePackages.add(ClassUtils.getPackageName(clazz));
        }
        //优先级 mapperHelperRef > properties > springboot
        String mapperHelperRef = value.getString("mapperHelperRef");
        String[] properties = value.getStringArray("properties");
        if (StringUtils.hasText(mapperHelperRef)) {
            scanner.setMapperHelperBeanName(mapperHelperRef);
        } else if (properties != null && properties.length > 0) {
            scanner.setMapperProperties(properties);
        } else {
            try {
                scanner.setMapperProperties(this.environment);
            } catch (Exception e) {

            }
        }
        scanner.registerFilters();
        scanner.doScan(StringUtils.toStringArray(basePackages));
    }
}
