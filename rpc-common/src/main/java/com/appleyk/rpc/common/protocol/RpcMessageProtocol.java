package com.appleyk.rpc.common.protocol;
import lombok.Data;

/**
 * <p>
 *  RPC消息传输协议，防止客户端与服务端通信时，获取tcp数据包时，出现粘包和拆包的问题
 *  出现原因：
 *  1、TCP是基于字节流的，虽然应用层和TCP传输层之间的数据交互是大小不等的数据块，
 *     但TCP却把这些数据块仅仅看成是一连串无结构的字节流，没有边界；
 *  2、从TCP自身的帧结构上可以看出来，在TCP的首部是没有表示数据长度的字段的
 *  解决办法：那就在传输消息的时候，人为（手动）的带上字节流的长度len，编解码器每次按"需"处理即可
 *        （白话：兄弟，我给你寄了2箱苹果，里面总过有125个，我给你放在物业了哈）
 * </p>
 *
 * @author appleyk
 * @version V.0.1.1
 * @blob https://blog.csdn.net/appleyk
 * @github https://github.com/kobeyk
 * @processOn示意图地址：https://processon.com/view/60364b14e0b34d1244395440
 * @date created on 16:25 2021/5/18
 */
@Data
public class RpcMessageProtocol {
    /**消息长度*/
    private int len;
    /**消息内容*/
    private byte[] data;
    public RpcMessageProtocol(){}
    public RpcMessageProtocol(byte[] data){
        this.data = data;
        this.len = data.length;
    }
}
