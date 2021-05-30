package com.appleyk.rpc.client;

import com.appleyk.rpc.common.util.*;
import com.appleyk.rpc.core.model.result.RpcRequest;
import com.appleyk.rpc.core.model.result.RpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetAddress;
import java.util.Date;

/**
 * <p>Rpc客户端 （负责构建netty客户端启动类，向rpc服务端发送请求）</p>
 * Rpc客户端通道处理器，主要负责和服务端建立通信，发送请求然后接收服务端发回来的响应对象并进行处理
 * @author appleyk
 * @version V.0.1.1
 * @blob https://blog.csdn.net/appleyk
 * @github https://github.com/kobeyk
 * @date created on 16:19 2021/5/19
 */
public class RpcClient extends SimpleChannelInboundHandler<RpcResponse> {

    private String ip;
    private int port;
    private RpcResponse response;

    public RpcClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public RpcClient(int port) {
        this.ip = "127.0.0.1";
        this.port = port;
    }

    /**
     * 发送（API）请求
     * @param request rpc封装的请求对象
     * @return rpc封装的请求响应对象
     */
    public RpcResponse send(RpcRequest request){
        /*默认cpu核数*2*/
        EventLoopGroup worker = new NioEventLoopGroup();
        try{
            Bootstrap client = new Bootstrap();
            client.group(worker)
                    .channel(NioSocketChannel.class)
                    .handler(new RpcClientChannelInitializer(this));
            /*等待异步执行完，拿到异步结果（变异步为同步）*/
            ChannelFuture channelFuture = client.connect(ip, port).sync();
            System.out.println("Rpc-Client,已与服务器建立连接,接下来准备发送数据...");
            /*获取客户端和服务端的通道*/
            Channel channel = channelFuture.channel();
            /*往通道里发送数据 -- RpcRequest对象*/
            channel.writeAndFlush(request);
            channel.closeFuture().sync();
        }catch (InterruptedException e){
            response.setE(e);
            response.setDate(new Date());
        }finally {
            System.out.println("#响应的结果："+ JsonUtils.parserJson(response));
            /*关闭线程池及清理内存空间，只发一次请求，不管失败还是异常，结果return之前，都要把线程池给关闭了*/
            worker.shutdownGracefully();
            return response;
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel serverChannel = ctx.channel();
        LoggerUtils.error("服务端：{}发送来的响应处理异常，{}\n",serverChannel.remoteAddress(),cause.getMessage());
        /*关闭通道处理器上下文*/
        ctx.close();
    }

    /**接收解码器的结果，并对响应做出处理 -- 读取*/
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse response) throws Exception {
        System.out.printf("接收来自服务端：{%s}的响应，时间：%s\n",ctx.channel().remoteAddress(), DateUtils.dateNow2Str());
        this.response = response;
        /*如果响应成功返回，则断开与server端的通道*/
        ctx.close();
    }

    /**简单测试*/
    public static void main(String[] args) throws Exception{
        InetAddress addr = InetAddress.getLocalHost();
        String ip = addr.getHostAddress();
        RpcClient client = new RpcClient(GeneralUtils.isEmpty(ip) ? "127.0.0.1":ip,8077);
        RpcRequest request = RpcRequest.builder()
                .requestId(IdUtils.getId())
                .interfaceName("com.appleyk.rpc.api.CacheService")
                .type("mongodb")
                .methodName("save")
                .parameterTypes(new Class[]{String.class, Object.class})
                .parameters(new Object[]{"name", "Appleyk"})
                .build();
        client.send(request);
    }
}
