package cn.bucheng.flink.checkpoint;
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

import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.apache.flink.util.Collector;

import java.util.LinkedList;
import java.util.List;

/**
 * @author yinchong
 * @create 2020/1/30 14:59
 * @description
 */
public class CountOperatorState extends RichFlatMapFunction<Long, String> implements CheckpointedFunction {

    private transient ListState<Long> checkPointList;
    private List<Long> bufferElements;

    public CountOperatorState() {
        bufferElements = new LinkedList<>();
    }

    @Override
    public void flatMap(Long value, Collector<String> out) throws Exception {
        if (value == 1L) {
            if (bufferElements.size() > 0) {
                StringBuilder sb = new StringBuilder();
                for (Long element : bufferElements) {
                    sb.append(element).append(" ");
                }
                out.collect(sb.toString());
                bufferElements.clear();
            }
        } else {
            bufferElements.add(value);
        }
    }

    @Override
    public void snapshotState(FunctionSnapshotContext context) throws Exception {
        checkPointList.clear();
        for (Long element : bufferElements) {
            checkPointList.add(element);
        }
        System.out.println("======snapShotState======,checkPointList:"+checkPointList);
    }

    @Override
    public void initializeState(FunctionInitializationContext context) throws Exception {
        ListStateDescriptor<Long> listStateDescriptor = new ListStateDescriptor<Long>("listStateTest", TypeInformation.of(new TypeHint<Long>() {
            @Override
            public TypeInformation<Long> getTypeInfo() {
                return super.getTypeInfo();
            }
        }));

        checkPointList = context.getOperatorStateStore().getListState(listStateDescriptor);
        if (context.isRestored()) {
            for (Long element : checkPointList.get()) {
                bufferElements.add(element);
            }
        }
    }
}
