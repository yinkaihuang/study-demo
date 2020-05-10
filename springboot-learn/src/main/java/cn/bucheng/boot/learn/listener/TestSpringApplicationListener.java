package cn.bucheng.boot.learn.listener;
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

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @author yinchong
 * @create 2020/4/20 22:17
 * @description
 */
@Slf4j
public class TestSpringApplicationListener implements SpringApplicationRunListener {
    private final SpringApplication application;
    private final String[] args;

    public TestSpringApplicationListener(SpringApplication application, String[] args) {
        this.application = application;
        this.args = args;
    }

    @Override
    public void starting() {
        log.info("test spring application listener starting invoke");
    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
        log.info("test spring application listener environmentPrepared invoke");
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        log.info("test spring application listener contextPrepared invoke");
    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
        log.info("test spring application listener contextLoaded invoke");
    }

    @Override
    public void started(ConfigurableApplicationContext context) {
        log.info("test spring application listener started invoke");
    }

    @Override
    public void running(ConfigurableApplicationContext context) {
        log.info("test spring application listener running invoke");
    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {
        log.info("test spring application listener failed invoke");
    }
}
