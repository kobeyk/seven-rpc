package com.appleyk.rpc.common.codec;

import com.appleyk.rpc.common.util.PbfSerializationUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * <p>消息编码器(消息对象转byte)</p>
 *
 * @author appleyk
 * @version V.0.1.1
 * @blob https://blog.csdn.net/appleyk
 * @github https://github.com/kobeyk
 * @date created on 16:44 2021/5/18
 */
public class RpcEncoder extends MessageToByteEncoder {

    /**字节流对应的源对象的类型*/
    private Class<?> sourceClass;

    public RpcEncoder(Class<?> sourceClass) {
        this.sourceClass = sourceClass;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object in, ByteBuf out) throws Exception {
        if (sourceClass.isInstance(in)){
            System.out.println("=== RpcEncoder#encode 方法被调用 ===");
            byte[] data = PbfSerializationUtils.serialize(in);
            // 把长度写进去
            out.writeInt(data.length);
            // 把内容写进去（注意写进去的长度是固定的,只要消息解码器按长度读取buf中的内容，就不会出现拆包or粘包问题）
            out.writeBytes(data);
        }
    }
}
