package cn.bucheng.gateway;


import cn.bucheng.gateway.bootstrap.FastGatewayBootstrap;

/**
 * @author yinchong
 * @create 2019/12/31 14:09
 * @description
 */
public class FastGatewayApplication {
    public static void main(String[] args) {
         new FastGatewayBootstrap().start(9090);
    }
}
