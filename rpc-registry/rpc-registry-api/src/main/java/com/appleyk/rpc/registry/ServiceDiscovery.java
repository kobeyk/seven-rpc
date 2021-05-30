package com.appleyk.rpc.registry;

/**
 * <p>服务发现接口</p>
 *
 * @author appleyk
 * @version V.0.1.1
 * @blob https://blog.csdn.net/appleyk
 * @github https://github.com/kobeyk
 * @date created on 12:28 2021/3/12
 */
public interface ServiceDiscovery {

    /**
     * 根据服务名，获取服务地址
     * @param serviceName 服务名称
     * @return 服务地址
     */
    String discover(String serviceName);
}
