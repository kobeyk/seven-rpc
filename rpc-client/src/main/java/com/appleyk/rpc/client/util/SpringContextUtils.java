package com.appleyk.rpc.client.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * <p>
 *     Spring 上下文工具类（简单版）：利用Spring（观察者模式）上下文监听器，
 *     对上下文刷新完事件进行监听，以回调onApplicationEvent方法顺便拿到全局的Spring的context对象
 * </p>
 *
 * @author appleyk
 * @version V.0.1.1
 * @blob https://blog.csdn.net/appleyk
 * @github https://github.com/kobeyk
 * @date created on 14:05 2021/5/26
 */
@Component
public class SpringContextUtils implements ApplicationListener<ContextRefreshedEvent> {

    public static ApplicationContext context;

    private SpringContextUtils(){}

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
         context = contextRefreshedEvent.getApplicationContext();
    }

    public static Object getBean(Class<?> beanClz){
        return context.getBean(beanClz);
    }
}
