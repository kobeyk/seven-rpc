package com.appleyk.rpc.client.autoconfigure;

import com.appleyk.rpc.client.annotion.RpcAutowired;
import com.appleyk.rpc.client.annotion.meta.RpcServiceAnnotationMetaData;
import com.appleyk.rpc.client.proxy.servicebean.RpcServiceBeanProxy;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;

/**
 * <p>自动装配rpc-client包中的bean</p>
 *
 * @author appleyk
 * @version V.0.1.1
 * @blob https://blog.csdn.net/appleyk
 * @github https://github.com/kobeyk
 * @date created on 17:13 2021/5/19
 */
@Configuration
@ComponentScan("com.appleyk.rpc.client")
public class RpcClientAutoConfigure {
    public RpcClientAutoConfigure() {
        System.out.println("=== RPC-CLIENT complete automatic assembly ！===");
    }

    @Bean
    public BeanPostProcessor beanPostProcessor(){
        return new BeanPostProcessor() {

            /**在bean完成初始化之前（注意，这时候bean已经完成实例化、且已经状态属性了）*/
            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
                Class<?> objClz;
                /*先判断下bean是不是Spring生成的代理对象*/
                if (AopUtils.isAopProxy(bean)){
                    /*获取目标对象*/
                    objClz = AopUtils.getTargetClass(bean);
                }else {
                    /*如果不是proxy对象，那就获取bean的class，主要用于下面的反射*/
                    objClz = bean.getClass();
                }
                try{
                    ReflectionUtils.doWithLocalFields(objClz,field -> {
                        RpcAutowired rpcAutowired = field.getAnnotation(RpcAutowired.class);
                        /*如果标注了这个注解，则表示bean注入的方式可以多样化*/
                        if (rpcAutowired != null){
                            field.setAccessible(true);
                            Class<?> type = field.getType();
                            RpcServiceAnnotationMetaData metaData = RpcServiceAnnotationMetaData.builder()
                                    .type(rpcAutowired.value())
                                    .group(rpcAutowired.group())
                                    .version(rpcAutowired.version()).build();
                            field.set(bean, RpcServiceBeanProxy.getObject(type,metaData));
                        }
                    });
                }catch (Exception e){
                    throw new BeanCreationException(beanName, e);
                }

                return bean;
            }
        };
    }
}
