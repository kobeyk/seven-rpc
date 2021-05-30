package com.appleyk.rpc.client;

import com.appleyk.rpc.common.codec.RpcDecoder;
import com.appleyk.rpc.common.codec.RpcEncoder;
import com.appleyk.rpc.core.model.result.RpcRequest;
import com.appleyk.rpc.core.model.result.RpcResponse;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * <p>Rpc客户端通道初始化器，用来给通信管道设置各种处理器的</p>
 *
 * @author appleyk
 * @version V.0.1.1
 * @blob https://blog.csdn.net/appleyk
 * @date created on  下午9:37 2021/5/22
 */
public class RpcClientChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final RpcClient client;

    public RpcClientChannelInitializer(RpcClient client) {
        this.client = client;
    }

    @Override
    protected void initChannel(SocketChannel sc) throws Exception {
        ChannelPipeline pipeline = sc.pipeline();
        pipeline.addLast(new RpcEncoder(RpcRequest.class));
        pipeline.addLast(new RpcDecoder(RpcResponse.class));
        pipeline.addLast(client);
    }
}
