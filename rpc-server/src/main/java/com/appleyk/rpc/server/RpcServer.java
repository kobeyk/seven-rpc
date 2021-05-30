package com.appleyk.rpc.server;

import com.appleyk.rpc.core.annotation.RpcService;
import com.appleyk.rpc.common.util.GeneralUtils;
import com.appleyk.rpc.common.util.LoggerUtils;
import com.appleyk.rpc.registry.ServiceRegistry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.NettyRuntime;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>RPC 服务端 ==> 绑定ip、端口、配置、启动netty服务...etc</p>
 *
 * 思路：
 *   1、服务启动前，要先拿到服务端所有的接口实现类（bean）
 *   2、要想获得所有的service，一是需要借助@RpcService注解以便Spring bean扫描，
 *      二是需要借助Spring上下文容器，也就是IOC Container，因为扫描后的bean都在它里面
 *   3、添加@RpcService注解很easy，直接在接口实现类上添加标记即可，那如何获取Spring的applicationContext呢?
 *      3.1 稍微看过Spring源码（BeanFactory顶层接口最上面的注释有关于bean的生命周期的各种类的功能描述）的人应该都清楚，
 *          借助ApplicationContextAware这个类，可以很轻松的在当前实现类中注入我们需要的context
 *      3.2 拿到context后，我们就拿到了bean的容器啊，里面不仅有各种bean的定义，还有各种bean（类的实例化对象）...etc
 *   4、有了Spring上下文后，结合@RpcService注解和Java反射技术，可以很容易的获取到bean的成员变量信息
 *   5、有了关于bean的一切后，我们就可以向ZK（zookeeper）注册我们的服务（API）了
 *   6、服务都注册好后，剩下的就是墨守陈规，使用netty api 配置和启动我们的rpc服务器了
 *
 * @author appleyk
 * @version V.0.1.1
 * @blob https://blog.csdn.net/appleyk
 * @github https://github.com/kobeyk
 * @date created on 10:36 2021/5/18
 */
@Component
public class RpcServer implements ApplicationContextAware, InitializingBean {

    /**
     * DI：注入服务注册接口（bean）,项目中内置了zk的实现方式，当然还可以用redis、consul、nacos等
     * 注意：多个实现类，注入的时候记得区分下
     */
    @Autowired
    private ServiceRegistry serviceRegistry;

    @Value("${server.port}")
    private int port;
    private String ip;
    private ApplicationContext applicationContext;
    /*key:接口名+实现接口的类的类型（两个合起来可以唯一确定一个bean），value：服务实现对应的bean*/
    private Map<String,Object> serviceMappings = new HashMap<>(16);

    /***
     * 扫描RPC服务包中所有带@RpcService注解的bean，然后将beanName和bean添加到服务映射中
     * @param context Spring上下文 == IOC Container
     */
    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        LoggerUtils.debug("RPC-SERVER，开始扫描Service");
        this.applicationContext = context;
        /**通过spring ioc容器，获取所有加了@RpcService对应的beansMap */
        Map<String, Object> serviceBeansMap = context.getBeansWithAnnotation(RpcService.class);
        /** 解析beansMap，一一向（分布式服务治理）zk注册服务（创建节点） */
        if (GeneralUtils.isEmpty(serviceBeansMap) || GeneralUtils.isEmpty(serviceBeansMap)){
            return;
        }
        try{
            /*获取本机IP*/
            InetAddress addr = InetAddress.getLocalHost();
            this.ip = addr.getHostAddress();
        }catch (Exception e){
            this.ip = "127.0.0.1";
        }
        String serviceAddress = ip +":"+port;
        for (Object bean : serviceBeansMap.values()) {
            String apiName = getServiceName(bean);
            /*往rpc服务端全局接口bean映射map中添加k-v*/
            this.serviceMappings.put(apiName,bean);
            /*向zk注册服务（创建服务节点）*/
            serviceRegistry.register(apiName,serviceAddress);
        }
    }

    private String getServiceName(Object bean) {
        String apiName;
        /*获取（接口）bean的注解*/
        RpcService rpcService = bean.getClass().getAnnotation(RpcService.class);
        /*分别获取注解的value（实现的接口类型）和name（接口实现类的名称或称作实现方式）值*/
        Class<?> value = rpcService.value();
        String name = rpcService.name();
        if (value.getName().equals("java.lang.Void")){
            /*如果value默认是Void类型的话，说明接口实现类上没有主动标记接口Type*/
            Type[] genericInterfaces = bean.getClass().getGenericInterfaces();
            if (genericInterfaces != null && genericInterfaces[0] != null){
                apiName = genericInterfaces[0].getTypeName();
            }else {
                throw new RuntimeException(String.format("无法获取{%s}接口类型！",bean.getClass().getName()));
            }
        }else{
            apiName = value.getName();
        }
        if (StringUtils.isNotEmpty(name)){
            apiName +="-"+name;
        }
        return apiName;
    }

    /**
     * 开始搞netty
     */
    @Override
    public void afterPropertiesSet() throws Exception {

        LoggerUtils.debug("RPC-SERVER，开始配置");
        /*1、首先分别创建"老板"和"工人"两个线程池(组),老板负责接项目（请求），工人负责干活（处理请求）*/
        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup worker = new NioEventLoopGroup(NettyRuntime.availableProcessors() * 2);
        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap
                    .group(boss,worker)
                    // 配置服务端IO通道类型 (客户端类型是：NioSocketChannel,注意区分)
                    .channel(NioServerSocketChannel.class)
                    // 服务端处理的请求线程满负荷时，用于临时存放tcp请求的队列最大长度
                    .option(ChannelOption.SO_BACKLOG,128)
                    // 开启客户端心跳监测机制，当服务端监测到客户端长时间"不坑声"时，就会发送一个ack探测包给对方，已确认
                    .childOption(ChannelOption.SO_KEEPALIVE,true)
                    // 配置"工人"线程组所需要的各种处理器（继承通道初始化器类即可）
                    .childHandler(new RpcServerChannelInitializer(serviceMappings));
            /*sync()能确保channelFuture一定是已经完成的*/
            ChannelFuture channelFuture = serverBootstrap.bind(ip, port).sync();
            System.out.printf("Rpc-Server,已启动,监听地址：%s：%d 目前正等待服务消费者请求....\n",ip,port);
            channelFuture.channel().closeFuture().sync();
        }catch (Exception ex){
            LoggerUtils.error("Rpc-Server服务器启动异常：{}", ex.getMessage());
        }finally {
            /*优雅的关闭线程池（断开连接 + 清理内存）*/
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
