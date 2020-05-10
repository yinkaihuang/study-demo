package cn.bucheng.gateway.client;

import cn.bucheng.gateway.model.SafeLinkedList;
import cn.bucheng.gateway.server.ProxyToServerChannelHandler;
import cn.bucheng.gateway.util.ResponseUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.GenericFutureListener;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author yinchong
 * @create 2020/4/26 20:41
 * @description 处理客户端到代理请求
 */
public class ClientToProxyChannelHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    /**
     * 用于存放当前线程上面 不同远程地址上面的连接池中连接
     */
    private static ThreadLocal<Map<InetSocketAddress, LinkedList<ProxyToServerChannelHandler>>> proxyToServerMap = ThreadLocal.withInitial(HashMap::new);

    /**
     * 获取当前线程池 某个远程地址的 http连接池
     *
     * @param address
     * @return
     */
    private LinkedList<ProxyToServerChannelHandler> proxyToServerChannelList(InetSocketAddress address) {
        return proxyToServerMap.get().computeIfAbsent(address, add -> new SafeLinkedList<>());
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        request.retain();
        Channel clientToProxyChannel = ctx.channel();
        InetSocketAddress address = route(request, clientToProxyChannel);
        if (address == null) {
            return;
        }
        LinkedList<ProxyToServerChannelHandler> proxyToServerChannelList = proxyToServerChannelList(address);
        ProxyToServerChannelHandler proxyToServerChannel = proxyToServerChannelList.removeFirst();
        if (proxyToServerChannel != null && proxyToServerChannel.isActive()) {
            proxyToServerChannel.writeProxyRequestToServer(request, clientToProxyChannel);
        } else {
            newProxyToServerChannel(address, request, clientToProxyChannel, proxyToServerChannelList);
        }
    }

    private void newProxyToServerChannel(InetSocketAddress address, FullHttpRequest request, Channel clientToProxyChannel, LinkedList<ProxyToServerChannelHandler> proxyToServerChannelList) {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(clientToProxyChannel.eventLoop())
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new HttpResponseDecoder())
                                .addLast(new HttpObjectAggregator(1024 * 1024 * 10))
                                .addLast(new HttpRequestEncoder())
                                .addLast(new ProxyToServerChannelHandler(ch, proxyToServerChannelList));
                    }
                })
                .connect(address)
                .addListener(new GenericFutureListener<ChannelFuture>() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        if (future.isSuccess()) {
                            future.channel().pipeline().get(ProxyToServerChannelHandler.class).writeProxyRequestToServer(request, clientToProxyChannel);
                        }
                    }
                });

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.close();
        super.channelInactive(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            switch (event.state()) {
                case ALL_IDLE:
                    ctx.channel().writeAndFlush(ResponseUtil.timeOut());
                    break;
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        super.exceptionCaught(ctx, cause);
    }

    private InetSocketAddress route(FullHttpRequest request, Channel channel) {
        return new InetSocketAddress("127.0.0.1", 9080);
//        channel.writeAndFlush(ResponseUtil.noRoute());
//        return null;
    }

}
