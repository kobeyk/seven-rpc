package com.appleyk.rpc.client.aop;

import com.appleyk.rpc.common.result.HttpResult;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * <p>AOP</p>
 *
 * @author appleyk
 * @version V.0.1.1
 * @blob https://blog.csdn.net/appleyk
 * @github https://github.com/kobeyk
 * @date created on 16:23 2021/5/19
 */
@Component
@Aspect // 标记这个类是切面
public class RpcAop {

    /**
     * 定义切点
     * 第一个星号，表示返回方法任返回意类型
     * 第一个..*，表示匹配appleyk包下面的任意个包
     * 第二个星号，表示匹配appleyk下面的任意包
     * 第三个星号，表示匹配controller包下面，以Controller结尾的任意前缀匹配
     * 第四个星号，表示匹配XXXController类下面的任意方法
     * 最后括号里面的两个..，表示任意参数
     */
    @Pointcut("execution(* com.appleyk.rpc..*.controller.*Controller.*(..))")
    public void rpcPointcut(){}

    @Around("rpcPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable{
        /*获取切入点签名（一个类的一个方法就是一个切点）*/
        Signature signature = joinPoint.getSignature();
        /*获取切入点签名的类型，就是方法所在的类*/
        Class<?> declaringType = signature.getDeclaringType();
        System.out.printf("AOP - {%s}.方法{%s}执行前\n",declaringType,signature.getName());
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object proceed = joinPoint.proceed();
        stopWatch.stop();
        System.out.printf("AOP - {%s}.方法{%s}执行耗时统计：{%d}ms\n",declaringType,signature.getName(),
                stopWatch.getTotalTimeMillis());

        if (declaringType.getName().endsWith("AopController")) {
            return HttpResult.ok(String.format("方法{%s-%s}执行耗时统计：{%d}ms",declaringType,signature.getName(),
                    stopWatch.getTotalTimeMillis()));
        }

        return proceed;
    }
}
