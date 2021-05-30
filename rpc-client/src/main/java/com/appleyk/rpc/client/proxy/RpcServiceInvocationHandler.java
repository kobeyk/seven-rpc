package com.appleyk.rpc.client.proxy;


import com.appleyk.rpc.client.RpcClient;
import com.appleyk.rpc.client.annotion.meta.RpcServiceAnnotationMetaData;
import com.appleyk.rpc.client.util.SpringContextUtils;
import com.appleyk.rpc.common.util.GeneralUtils;
import com.appleyk.rpc.common.util.IdUtils;
import com.appleyk.rpc.common.util.LoggerUtils;
import com.appleyk.rpc.core.model.result.RpcRequest;
import com.appleyk.rpc.core.model.result.RpcResponse;
import com.appleyk.rpc.registry.ServiceDiscovery;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * <p>动态代理，真正实现被代理"对象"的方法调用的处理器</p>
 *
 * @author appleyk
 * @version V.0.1.1
 * @blob https://blog.csdn.net/appleyk
 * @date created on  下午9:30 2021/5/19
 */
public class RpcServiceInvocationHandler implements InvocationHandler{

    /*服务发现对象，从ioc容器里取*/
    private ServiceDiscovery serviceDiscovery;

    private RpcServiceAnnotationMetaData metaData;

    public RpcServiceInvocationHandler(){
    }

    public RpcServiceInvocationHandler(RpcServiceAnnotationMetaData metaData){
        this.metaData = metaData;
    }

    public RpcServiceInvocationHandler(ApplicationContext context){
        ServiceDiscovery discovery = context.getBean(ServiceDiscovery.class);
        if (discovery==null){
            throw new RuntimeException("serviceRegistry bean not found !");
        }
        this.serviceDiscovery = discovery;
    }

    /**
     * 整个RPC服务消费方·最最最重要的地方了，也是接口"实例"被代理后，就具有了"本地调用"的奥秘所在
     * @param proxy 代理对象
     * @param method 方法
     * @param args 参数
     * @return 方法执行结果
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        /*如果是当前类的实例自己调用自己的invoke方法的话，那就直接返回*/
        if (method.getDeclaringClass().equals(this)){
            return method.invoke(proxy,args);
        }

        if (serviceDiscovery == null){
            this.serviceDiscovery = (ServiceDiscovery) SpringContextUtils.getBean(ServiceDiscovery.class);
        }

        /*1、获取方法所在类（其实是接口）的名称（这里是全限定名，即包名+类名）*/
        String apiName= method.getDeclaringClass().getName();
        String serverAddress = serviceDiscovery.discover(apiName);
        /*2、验证服务地址合法性*/
        if (GeneralUtils.isEmpty(serverAddress)|| serverAddress.split(":").length != 2){
            LoggerUtils.error("未发现{}对应有可用的服务！，请稍后再试",apiName);
            return null;
        }
        /*3、获取接口类型*/
        String apiType = metaData != null ? metaData.getType() : "";
        /*4、分割服务地址，分别获取ip和port*/
        String[] host = serverAddress.split(":");
        String ip = host[0];
        int port = Integer.parseInt(host[1]);
        /*5、基于ip和port，构建client（netty）*/
        RpcClient client = new RpcClient(ip,port);
        /*6、基于被代理的对象的信息构建请求请求对象*/
        long requestId = IdUtils.getId();
        RpcRequest request = RpcRequest.builder()
                .requestId(requestId)
                .interfaceName(apiName)
                .methodName(method.getName())
                .type(apiType) // 这个很关键，因为接口可能有多个实现类，ZK上一个节点就是一个服务，区别接口实现类的唯一标识就是这个type
                .parameterTypes(method.getParameterTypes())
                .parameters(args)
                .build();
        /*7、发送请求（与netty服务端建立连接，并往双方的通信通道里传输数据，然后服务端解码处理后返回响应结果）*/
        RpcResponse response = client.send(request);
        /*8、验证请求ID是不是合法的，就好比，你要点一杯咖啡，服务员却给你来了一杯冰红茶*/
        if (requestId != response.getRequestId()){
            throw new RuntimeException("请求与响应的requestId不对应，无法确定结果是不是合法！");
        }
        /*9、被代理的对象是接口类型的bean，其无法实例化，更谈不上有返回值了，所以只能从rpc调用的结果里取出返回值并返回出去*/
        return response.getResult();
    }
}
