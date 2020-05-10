package cn.bucheng.gateway.util;


import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * @author yinchong
 * @create 2020/4/27 19:59
 * @description
 */
public class ResponseUtils {

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
