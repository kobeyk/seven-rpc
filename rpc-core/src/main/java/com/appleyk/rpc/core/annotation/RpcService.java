package com.appleyk.rpc.core.annotation;

import org.springframework.stereotype.Service;

import java.lang.annotation.*;

/**
 * <p>在接口实现类上加上该注解，使其具有rpc和spring bean的功能</p>
 *
 * @author appleyk
 * @version V.0.1.1
 * @blob https://blog.csdn.net/appleyk
 * @github https://github.com/kobeyk
 * @date created on 10:40 2021/5/18
 */
@Target(ElementType.TYPE) // 标注在类上
@Retention(RetentionPolicy.RUNTIME) // 保留到JVM运行时，即字节码加载到内存后，注解依然"存活"
@Documented // 是否生成javadoc时，一并把注解属性显示在标记的类上，加了就带，不加就不带
@Service// 可以带这个，也可以不带这个，带这个是为了简化开发人员的"工作量",每次都少写一个@Service，它不香吗
public @interface RpcService {

    /* 先空着，暂时没想好放什么，后续功能扩充了，会补一些方法 2021年5月19日 15:53:20*/

    /*
     * 2021年05月20日20:49:43
     * 想了想，接口只有一个啊，实现类可是有多个啊，面向对象的三大特性、封装、继承、多态
     * 如果你平时在coding练习或者在公司做项目时，连接口的多态性都没有听过或实操过的话，
     * 显然是让人无法接受的，话说，我们这个rpc项目里，干嘛要提一嘴这个呢，原因很简单：
     * 1、有一个公共的API模块
     * 2、客户端（服务消费者）只需要知道公共的API怎么用就行，无需关心它是怎么实现的
     *   ，也不要给我整什么让我手动去创建代理对象，然后再rpc，我就想简单、简单、简单调用！！！
     * 3、服务端（服务提供者）需要实现公共API接口中的方法，比如，缓存接口的增删改查，
     *    实现方式可以是redis、mongodb、甚至是memberCache..etc
     * 4、那客户端在只知道有个缓存接口，比如XXXCacheService的情况下，要怎么知道该调用（服务端）哪个接口的实现（多态体现）呢？
     * 5、索性，我们在每个接口实现类上主动的标注下它的接口类型即可，同时带上它的实现方式，如下：
     *    5.1
     *    @RpcService(ICacheService.class,name="mongodb")
     *    public MongoDbCacheServiceImpl implements ICacheService{
     *         byte[] getData(String key);
     *    }
     * 6、如果不显示标注它的接口类型的话，那就由程序通过xxxServiceImpl.getClass().getGenericInterfaces()的方式获取
     *   (简单起见，本rpc框架中，不涉及一个类实现多个接口的场景，单一职责，业务接口实现类很专一，一个实现类就对应实现一个接口)
     *
     */
    Class<?> value() default Void.class;
    String name() default "";
    String version() default "0.1.1";
}
