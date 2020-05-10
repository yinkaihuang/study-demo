package cn.bucheng.gateway.bootstrap;

import cn.bucheng.gateway.client.ClientToProxyChannelHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author yinchong
 * @create 2020/4/26 21:10
 * @description
 */
public class FastGatewayBootStrap {

    public void start(int port) {
        //bossGroup专门用于建立client到proxy连接
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        //用于处理client过来的请求
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new IdleStateHandler(0, 0, 15))
                                .addLast(new HttpRequestDecoder())
                                .addLast(new HttpObjectAggregator(1024 * 1024 * 10))
                                .addLast(new HttpResponseEncoder())
                                .addLast(new ClientToProxyChannelHandler());
                    }
                });

        try {
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            System.out.println("FastGateway start success bind port in "+port);
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
