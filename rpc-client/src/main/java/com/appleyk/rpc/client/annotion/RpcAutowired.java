package com.appleyk.rpc.client.annotion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Rpc 自动注入 注解</p>
 *
 * @author appleyk
 * @version V.0.1.1
 * @blob https://blog.csdn.net/appleyk
 * @date created on  下午10:23 2021/5/20
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcAutowired {
    String value() default ""; // 注入的bean名称，比如cache可以是mongodb或redis实现
    String version() default ""; // 注入的bean（服务、接口）的版本
    String group() default ""; // 注入bean（服务、接口）的组
}
