package com.appleyk.rpc.common.excp;

/**
 * <p>接口未发现</p>
 *
 * @author appleyk
 * @version V.0.1.1
 * @blob https://blog.csdn.net/appleyk
 * @date created on  下午9:45 2021/5/23
 */
public class RpcApiNotFoundException extends Exception{
    public RpcApiNotFoundException(String message){
        super(message);
    }
}
