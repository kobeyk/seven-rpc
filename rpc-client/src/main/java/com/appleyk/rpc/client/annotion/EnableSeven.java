package com.appleyk.rpc.client.annotion;

import com.appleyk.rpc.client.registrar.RpcServiceBeanDefinitionRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Rpc接口包扫描·注解</p>
 *
 * @author appleyk
 * @version V.0.1.1
 * @blob https://blog.csdn.net/appleyk
 * @github https://github.com/kobeyk
 * @date created on 17:35 2021/5/19
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(RpcServiceBeanDefinitionRegistrar.class) // 在这个bean定义注册类里，实现接口的动态代理
public @interface EnableSeven {
    /*你需要扫描的beans所在的根包*/
    String scanBasePackages() default "";
}
