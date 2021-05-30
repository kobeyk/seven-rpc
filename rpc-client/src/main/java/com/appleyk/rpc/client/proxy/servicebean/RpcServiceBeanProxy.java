package com.appleyk.rpc.client.proxy.servicebean;

import com.appleyk.rpc.client.annotion.meta.RpcServiceAnnotationMetaData;
import com.appleyk.rpc.client.proxy.RpcServiceInvocationHandler;

import java.lang.reflect.Proxy;

/**
 * <p>
 *    第二种代理方法：使用jdk自带的Proxy类来创建接口类型bean的代理对象
 *    代理时机：借助我们自定义的注入注解@RpcAutowired，复写bean后置处理器（实现BeanPostProcessor接口）
 *    的postProcessBeforeInitialization方法，找到带有@RpcAutowired的field，然后对field进行动态代理
 * </p>
 *
 * @author appleyk
 * @version V.0.1.1
 * @blob https://blog.csdn.net/appleyk
 * @github https://github.com/kobeyk
 * @date created on 14:01 2021/5/26
 */
public class RpcServiceBeanProxy {

    public static Object getObject(Class<?> targetClass){
        Object o = Proxy.newProxyInstance(
                targetClass.getClassLoader(),
                new Class<?>[]{targetClass},
                new RpcServiceInvocationHandler());
        return o;
    }

    public static Object getObject(Class<?> targetClass, RpcServiceAnnotationMetaData metaData){
        Object o = Proxy.newProxyInstance(
                targetClass.getClassLoader(),
                new Class<?>[]{targetClass},
                /*把bean的注解元数据带过去*/
                new RpcServiceInvocationHandler(metaData));
        return o;
    }


}
