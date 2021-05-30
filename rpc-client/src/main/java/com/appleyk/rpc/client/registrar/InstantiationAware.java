package com.appleyk.rpc.client.registrar;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * <p>bean实例化/初始化前后感知类增强器，这个类在这个框架里可以忽略，是基于Spring的测试类，就是玩的</p>
 *
 * @author appleyk
 * @version V.0.1.1
 * @blob https://blog.csdn.net/appleyk
 * @github https://github.com/kobeyk
 * @date created on 15:50 2021/5/21
 */
@Component
public class InstantiationAware implements InstantiationAwareBeanPostProcessor {

    /**
     * bean实例化之前（增强器，这个地方其实也可以对beanClass做代理）
     * @param beanClass bean类类型
     * @param beanName bean名称
     * @return 增强后的对象（如果不空，就是"偷天换日"了，成功代理了源bean）
     */
    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        if ("cacheService".equals(beanName)){
            System.out.printf("InstantiationAware#postProcessBeforeInstantiation,bean{%s}实例化之前\n",beanName);
        }
        return null; // 空的话，继续交给spring容器对原有bean进行实例化
    }

    /**
     * bean完成实例化后的增强器
     * @param bean bean对象
     * @param beanName bean名称
     */
    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        if ("cacheService".equals(beanName)){
            System.out.printf("InstantiationAware#postProcessBeforeInstantiation,bean{%s}实例化之后\n",beanName);
        }
        return true;
    }

    /**
     * 对属性值进行修改，如果postProcessAfterInstantiation方法返回false，
     * 该方法不会被调用。可以在该方法内对属性值进行修改
     */
    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        return null;
    }

    /**
     * 在Bean的自定义初始化方法执行完成之前执行
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    /**
     * 在Bean的自定义初始化方法执行完成之后执行
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
