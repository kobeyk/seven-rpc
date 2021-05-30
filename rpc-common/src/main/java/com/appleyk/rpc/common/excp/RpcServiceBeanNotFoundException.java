package com.appleyk.rpc.common.excp;

/**
 * <p>Rpc服务bean未发现异常</p>
 *
 * @author appleyk
 * @version V.0.1.1
 * @blob https://blog.csdn.net/appleyk
 * @date created on  下午10:15 2021/5/23
 */
public class RpcServiceBeanNotFoundException extends Exception{
    public RpcServiceBeanNotFoundException(String message){
        super(message);
    }
}
