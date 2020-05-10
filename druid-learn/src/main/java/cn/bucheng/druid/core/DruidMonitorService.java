package cn.bucheng.druid.core;
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

import com.alibaba.druid.stat.DruidStatService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.util.Strings;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author yinchong
 * @create 2020/3/19 8:37
 * @description
 */
public class DruidMonitorService {
    public static final String SQL_QUERY_DETAIL = "/sql.json";
    public static final String REST_ALL = "/reset-all.json";
    public static final String WALL_QUERY = "/wall.json";

    private DruidStatService service;

    private static DruidMonitorService instance = new DruidMonitorService();

    public static DruidMonitorService getInstance() {
        return instance;
    }

    private DruidMonitorService() {
        service = DruidStatService.getInstance();
    }


    /**
     * 上报并清空
     *
     * @return
     */
    public String reportAndClear() {
        String result = service.service(SQL_QUERY_DETAIL);
        String wallResult = service.service(WALL_QUERY);
        System.out.println(wallResult);
        DruidStatService.getInstance().service(REST_ALL);
        return result;
    }

    public List<SQLMonitorDTO> reportMonitor() {
        String s = reportAndClear();
        if (Strings.isBlank(s)) {
            return null;
        }
        JSONObject object = JSON.parseObject(s);
        Integer code = object.getInteger("ResultCode");
        if (code != 1) {
            return null;
        }
        JSONArray array = object.getJSONArray("Content");
        if (array == null || array.size() == 0) {
            return null;
        }
        Iterator<Object> iterator = array.iterator();
        List<SQLMonitorDTO> monitorDTOList = new LinkedList<>();
        while (iterator.hasNext()) {
            JSONObject next = (JSONObject) iterator.next();
            SQLMonitorDTO monitorDTO = next.toJavaObject(SQLMonitorDTO.class);
            monitorDTOList.add(monitorDTO);
            monitorDTO.setApp(EnviromentUtils.getValue("spring.application.name"));
        }
        return monitorDTOList;
    }
}
