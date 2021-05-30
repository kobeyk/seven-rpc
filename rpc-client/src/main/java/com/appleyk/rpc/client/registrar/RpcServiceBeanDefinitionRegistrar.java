package com.appleyk.rpc.client.registrar;

import com.appleyk.rpc.client.annotion.EnableSeven;
import com.appleyk.rpc.client.proxy.factorybean.RpcServiceFactoryBean;
import com.appleyk.rpc.client.sanner.RpcServiceBeanPathScanner;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 *     动态注册BeanDefinition到ioc容器：定制化 rpc service bean definition
 *     里面很关键的一点就是：利用我们自定义的bean包扫描类，对候选bean定义进行条件放行，
 *     比如对接口类型的bean定义进行放行，同时，对放行后的bean定义进行修改，
 *     将其原有的接口类型指向为一个FactoryBean类型，完成bean的华丽转身！！！
 * </p>
 *
 * @author appleyk
 * @version V.0.1.1
 * @blob https://blog.csdn.net/appleyk
 * @github https://github.com/kobeyk
 * @date created on 17:39 2021/5/19
 */
public class RpcServiceBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    /*代理类*/
    private static Class<?> targetClass = RpcServiceFactoryBean.class;
    private static final String BASE_PACKAGE = "scanBasePackages";
    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        Map<String, Object> attributes = metadata.getAnnotationAttributes(EnableSeven.class.getName());
        String basePackage = attributes.get(BASE_PACKAGE).toString();
        RpcServiceBeanPathScanner beanPathScanner = new RpcServiceBeanPathScanner(registry);
        beanPathScanner.addIncludeFilter(new TypeFilter() {
            @Override
            public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
                /*管他三七二十一，条件放开*/
                return true;
            }
        });
        /*扫描公共接口包，获取其下的所有的接口bean定义*/
        Set<BeanDefinitionHolder> beanDefinitionHolders = beanPathScanner.doScan(basePackage);
        for (BeanDefinitionHolder definitionHolder : beanDefinitionHolders) {
            GenericBeanDefinition bd = (GenericBeanDefinition) definitionHolder.getBeanDefinition();
            /*拿到service（接口）的类名*/
            String sourceClass = bd.getBeanClassName();
            System.out.printf("在bean{%s}还没有实例化之前，先对bean的class进行替换（狸猫换太子）\n",sourceClass);
            /*在service (接口) 还没有开始bean的实例化之前，"篡改"它的类型*/
            bd.setBeanClass(targetClass);
            /*类型都改了，下面顺带给targetClass类的有参构造器传参数，将bd的源类传进去，方便后续代理用*/
            bd.getConstructorArgumentValues().addIndexedArgumentValue(0,sourceClass);
        }
    }
}
