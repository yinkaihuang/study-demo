package cn.bucheng.gateway.server;


import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.LinkedList;

/**
 * @author yinchong
 * @create 2020/4/26 20:43
 * @description 处理proxy到服务端的请求
 */
public class ProxyToServerChannelHandler extends SimpleChannelInboundHandler<FullHttpResponse> {

    private Channel proxyToServerChannel;
    private Channel clientToProxyChannel;
    private boolean keepAlive;
    private LinkedList<ProxyToServerChannelHandler> queue;

    private GenericFutureListener<ChannelFuture> futureListener = new GenericFutureListener<ChannelFuture>() {

        @Override
        public void operationComplete(ChannelFuture future) throws Exception {
            if (future.isSuccess()) {
                recycle();
                if (!keepAlive) {
                    future.channel().close();
                }
            }
        }
    };

    public ProxyToServerChannelHandler(NioSocketChannel proxyToServerChannel, LinkedList<ProxyToServerChannelHandler> proxyToServerChannelList) {
        this.proxyToServerChannel = proxyToServerChannel;
        this.queue = proxyToServerChannelList;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse response) throws Exception {
        response.retain();
        clientToProxyChannel.writeAndFlush(response).addListener(futureListener);
    }

    public boolean isActive() {
        if (proxyToServerChannel != null && proxyToServerChannel.isActive()) {
            return true;
        }
        release();
        ;
        return false;
    }

    public void writeProxyRequestToServer(FullHttpRequest request, Channel clientToProxyChannel) {
        if (HttpUtil.isKeepAlive(request)) {
            keepAlive = true;
        } else {
            keepAlive = false;
        }
        this.clientToProxyChannel = clientToProxyChannel;
        proxyToServerChannel.attr(AttributeKey.valueOf("HTTP_REQUEST")).set(request);
        proxyToServerChannel.writeAndFlush(request);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        release();
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        release();
        super.exceptionCaught(ctx, cause);
    }

    private void recycle() {
        queue.addLast(this);
    }

    private void release() {
        queue.remove(this);
    }
}
