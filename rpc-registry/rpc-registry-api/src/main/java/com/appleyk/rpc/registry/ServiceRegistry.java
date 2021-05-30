package com.appleyk.rpc.registry;

/**
 * <p>服务注册接口</p>
 *
 * @author appleyk
 * @version V.0.1.1
 * @blob https://blog.csdn.net/appleyk
 * @github https://github.com/kobeyk
 * @date created on 12:39 2021/3/12
 */
public interface ServiceRegistry {

    /**
     * 注册服务名称和服务地址
     *
     * @param serviceName 服务名称
     * @param serviceAddress 服务地址
     */
    void register(String serviceName,String serviceAddress);

}
