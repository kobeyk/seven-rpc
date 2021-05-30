package com.appleyk.rpc.server;

import com.appleyk.rpc.common.excp.RpcApiNotFoundException;
import com.appleyk.rpc.common.excp.RpcServiceBeanNotFoundException;
import com.appleyk.rpc.common.util.DateUtils;
import com.appleyk.rpc.common.util.GeneralUtils;
import com.appleyk.rpc.common.util.LoggerUtils;
import com.appleyk.rpc.core.model.result.RpcRequest;
import com.appleyk.rpc.core.model.result.RpcResponse;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

/**
 * <p>RPC服务端通道消息处理器</p>
 *
 * @author appleyk
 * @version V.0.1.1
 * @blob https://blog.csdn.net/appleyk
 * @github https://github.com/kobeyk
 * @date created on 16:06 2021/5/18
 */
public class RpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private final Map<String,Object> springContext;

    public RpcServerHandler(Map<String, Object> springContext) {
        this.springContext = springContext;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("请求(连接)进来了!");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("请求（连接）断开了!");
    }

    /**接收客户端（服务消费者）发过来的消息，处理request请求*/
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest request) throws Exception {
        Channel channel = ctx.channel();
        System.out.printf("接收来自客户端：{%s}的请求，时间：%s\n",channel.remoteAddress(), DateUtils.dateNow2Str());
        /*基于request，反射，调用相应的接口*/
        try{
            Object result = requestHandler(request);
            /*包装响应对象返回给客户端*/
            RpcResponse response = RpcResponse.builder()
                    .requestId(request.getRequestId())
                    .date(new Date())
                    .result(result).build();
            channel.writeAndFlush(response);
        }catch (Exception ex){
            RpcResponse response = RpcResponse.builder()
                    .requestId(request.getRequestId())
                    .date(new Date())
                    .result(null)
                    .e(ex).build();
            channel.writeAndFlush(response);
        }
    }

    private Object requestHandler(RpcRequest request) throws Exception {

        if (!isValid(request)){
            throw new RpcApiNotFoundException("请求传参出了问题，无法识别出接口或要调用的接口方法，请核查！");
        }

        /*接口名称*/
        String interfaceName = request.getInterfaceName();
        /*接口实现类的类型*/
        String serviceImplType = request.getType();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();
        String apiName = GeneralUtils.isEmpty(serviceImplType) ? interfaceName : interfaceName+"-"+serviceImplType;
        Object serviceBean = springContext.get(apiName);
        if (serviceBean == null) {
            throw new RpcServiceBeanNotFoundException(String.format("This interface {%s} does not exist !", apiName));
        }
        Class<?> beanClass = serviceBean.getClass();
        try {
            /*基于反射技术，拿到对象的指定方法名指定参数类的方法对象*/
            Method method = beanClass.getMethod(methodName, parameterTypes);
            /*基于反射技术，调用方法对象的invoke方法（反向操作就是我们直接用实例来调用它的方法）*/
            return method.invoke(serviceBean, parameters);
        }catch (Exception e){
            throw new RuntimeException("利用反射调用bean的方法异常！"+e.getMessage());
        }
    }

    private boolean isValid(RpcRequest request){
        /*先解析一波request，验证下是不是有没有漏传的内容，比如接口和方法名没有传..e.tc*/
        System.out.println("#请求唯一ID："+request.getRequestId());
        System.out.println("#接口名称："+request.getInterfaceName());
        System.out.println("#接口实现类类型："+request.getType());
        System.out.println("#接口调用方法："+request.getMethodName());
        return GeneralUtils.isNotEmpty(request.getRequestId())
                && GeneralUtils.isNotEmpty(request.getInterfaceName())
                && GeneralUtils.isNotEmpty(request.getMethodName());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel clientChannel = ctx.channel();
        LoggerUtils.error("客户端：{}发送来的请求处理异常，{}\n",clientChannel.localAddress(),cause.getMessage());
        /*关闭通道处理器上下文*/
        ctx.close();
    }
}
