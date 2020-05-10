package cn.bucheng.boot.learn.lifecycle;
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
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

/**
 * @author yinchong
 * @create 2020/4/20 21:59
 * @description
 */
@Component
@Slf4j
public class TestSmartLifeCycle implements SmartLifecycle {
    volatile boolean runFlag = false;

    @Override
    public void start() {
        if(runFlag){
            log.warn("test smart lifecycle already have started");
            return;
        }
        runFlag = true;
        log.info("test smart lifecycle is starting");
    }

    @Override
    public void stop() {
       runFlag = false;
       log.info("test smart lifecycle is stopping");
    }

    @Override
    public boolean isRunning() {
        return runFlag;
    }
}
