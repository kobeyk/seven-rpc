package com.appleyk.rpc.server;

import com.appleyk.rpc.common.codec.RpcDecoder;
import com.appleyk.rpc.common.codec.RpcEncoder;
import com.appleyk.rpc.common.util.LoggerUtils;
import com.appleyk.rpc.core.model.result.RpcRequest;
import com.appleyk.rpc.core.model.result.RpcResponse;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

import java.util.Map;

/**
 * <p>Rpc服务端通道处理器</p>
 *
 * @author appleyk
 * @version V.0.1.1
 * @blob https://blog.csdn.net/appleyk
 * @github https://github.com/kobeyk
 * @date created on 15:41 2021/5/18
 */
public class RpcServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final Map<String,Object> ioc ;

    public RpcServerChannelInitializer(Map<String, Object> ioc) {
        this.ioc = ioc;
    }

    @Override
    protected void initChannel(SocketChannel sc) throws Exception {
        /*从服务端通信通道中获取其关联的管道对象*/
        ChannelPipeline pipeline = sc.pipeline();
        try {
            /*往管道里添加消息解码器*/
            pipeline.addLast(new RpcDecoder(RpcRequest.class));
            /*往管道里添加消息编码器*/
            pipeline.addLast(new RpcEncoder(RpcResponse.class));
            /*往管道里添加RPC自己的消息处理器*/
            pipeline.addLast(new RpcServerHandler(ioc));
        } catch (Exception e) {
            LoggerUtils.error(e.getMessage());
        }
    }
}
