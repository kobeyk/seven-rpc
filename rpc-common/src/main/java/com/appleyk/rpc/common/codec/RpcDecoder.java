package com.appleyk.rpc.common.codec;

import com.appleyk.rpc.common.util.PbfSerializationUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * <p>消息解码器(字节流转消息)</p>
 * @author appleyk
 * @version V.0.1.1
 * @blob https://blog.csdn.net/appleyk
 * @github https://github.com/kobeyk
 * @date created on 16:43 2021/5/18
 */
public class RpcDecoder extends ByteToMessageDecoder {

    /**字节流对应的源对象的类型*/
    private Class<?> sourceClass;

    public RpcDecoder(Class<?> sourceClass) {
        this.sourceClass = sourceClass;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("=== RpcDecoder#decode 方法被调用 ===");
        /*实际buf中可读取的内容长度*/
        int readableBytes = in.readableBytes();
        if (readableBytes < 4){
            // 如果不满4个字节，就返回（最低要求都达不到，浪费感情，你不配继续解码！！！）
            return;
        }
        /*标记下开始读的位置*/
        in.markReaderIndex();
        /*由于编码器，将len写进了buf中，所以解码器这边读取下，获取下远端发过来的数据的长度（是否可信有待验证）*/
        int len = in.readInt();
        /*注意，实际缓冲区可读的（readableBytes）一定会大于buf的真实数据的长度，所以下面不要用等号判断*/
        if (readableBytes < len){
            /*重置下读指针，这时候如果不相等，只能说明数据没按要求传过来*/
            in.resetReaderIndex();
            byte[] content = new byte[readableBytes];
            in.readBytes(content);
            System.out.printf("抱歉，监测到你传过来的数据{%s}有问题（" +
                    "可读字节内容长度和指定buf中的字节长度不匹配），本次请求视为无效！\n",new String(content));
            /*别忘了再重置下，万一有其他handler需要再读取数据处理呢*/
            in.resetReaderIndex();
            /*清空*/
            in.clear();
            return;
        }
        // 通过长度获取对应字节（序列化直接诶）数组
        byte[] data = new byte[len];
        // 将缓冲中的数据读取到字节数组中（注意，这里读取的是特定长度的，所以不会出现拆包或粘包问题）
        in.readBytes(data);
        // 将读取到的流按类型反序列化成对象并放入到list中传给下一个handler进行处理
        out.add(PbfSerializationUtils.deserialize(data,sourceClass));
    }
}
