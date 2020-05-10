package cn.bucheng.gateway.util;
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

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * @author yinchong
 * @create 2020/4/27 19:59
 * @description
 */
public class ResponseUtil {

    public static String body(int code, String message) {
        String result = "{\"code\":" + code + ",\"msg\":\"" + message + "\"}";
        return result;
    }

    /**
     * 执行超时
     *
     * @return
     */
    public static FullHttpResponse timeOut() {
        String body = body(33, "执行超时");
        return response(480, body);
    }

    public static FullHttpResponse noRoute() {
        String body = body(22, "未发现路由");
        return response(480, body);
    }

    public static FullHttpResponse response(int status, String body) {
        HttpResponseStatus httpStatus = HttpResponseStatus.valueOf(status);
        FullHttpResponse resp = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                httpStatus,
                Unpooled.copiedBuffer(body, CharsetUtil.UTF_8));
        resp.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN,"*");
        resp.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json;charset=UTF-8");
        resp.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, resp.content().readableBytes());
        return resp;
    }
}
