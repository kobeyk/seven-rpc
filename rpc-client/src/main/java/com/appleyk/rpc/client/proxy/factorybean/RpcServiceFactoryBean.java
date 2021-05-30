package com.appleyk.rpc.client.proxy.factorybean;

import com.appleyk.rpc.client.proxy.RpcServiceInvocationHandler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Proxy;

/**
 * <p>
 *
 *  第一种代理方式: 通过FactoryBean的方式，代理时机，在注册bean定义的时候，"篡改"接口的class
 *
 *  个性化定制rpc service bean
 * （动态代理接口，使得客户端可以在本地直接使用接口调用远程的方法，让用户完全感觉不出来！！）
 *
 *  这种使用方式有个局限性，就是如果接口有多个实现，
 * </p>
 *
 * @author appleyk
 * @version V.0.1.1
 * @blob https://blog.csdn.net/appleyk
 * @date created on  下午9:21 2021/5/19
 */
public class RpcServiceFactoryBean<T> implements FactoryBean<T>,ApplicationContextAware {

    private final Class<T> targetClass;
    private ApplicationContext context;

    public RpcServiceFactoryBean(Class<T> targetClass) {
        this.targetClass = targetClass;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context  = applicationContext;
    }

    /**
     * 使用jdk动态代理创建一个对象，并返回
     * @return 代理对象
     */
    @Override
    public T getObject() {
        Object o = Proxy.newProxyInstance(
                targetClass.getClassLoader(),
                new Class<?>[]{targetClass},
                /*把spring ioc容器带过去*/
                new RpcServiceInvocationHandler(context));
        return (T) o;
    }

    @Override
    public Class<T> getObjectType() {
        return targetClass;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
