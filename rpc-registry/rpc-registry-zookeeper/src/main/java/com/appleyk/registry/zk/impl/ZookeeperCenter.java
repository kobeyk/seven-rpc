package com.appleyk.registry.zk.impl;

import com.appleyk.registry.zk.util.ZkApiUtils;
import com.appleyk.rpc.registry.ServiceDiscovery;
import com.appleyk.rpc.registry.ServiceRegistry;
import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

/**
 * <p>基于ZK实现的服务注册与发现中心</p>
 *
 * @author appleyk
 * @version V.0.1.1
 * @blob https://blog.csdn.net/appleyk
 * @github https://github.com/kobeyk
 * @date created on 12:51 2021/3/12
 */
@Service
public class ZookeeperCenter implements ServiceRegistry, ServiceDiscovery {

    private static final Logger logger = LoggerFactory.getLogger(ZookeeperCenter.class);

    @Autowired
    private CuratorFramework client;

    public String discover(String serviceName) {
        String serviceNamePath = "/"+serviceName;
        /*获取服务目录节点下面的API子节点（多个）*/
        List<String> children = ZkApiUtils.getPathChildren(client, serviceNamePath);
        if (children == null || children.size() == 0){
            /*空就意味着注册中心没有任何服务接口提供给服务消费方*/
            return "";
        }
        String childPath = children.get(choose(children.size()));
        String childFullPath = serviceNamePath+"/"+childPath;
        return ZkApiUtils.getNodeValue(client, childFullPath);
    }

    /**
     * 随机返回服务接口列表中的某一个地址的索引下标
     * @param len 列表长度
     */
    public int choose(int len){
        return new Random().nextInt(len);
    }

    /**
     * 实现服务注册功能，关键字（永久、顺序）
     * @param serviceName 服务名称
     * @param serviceAddress 服务地址
     */
    public void register(String serviceName, String serviceAddress) {
        String serviceNamePath = "/"+serviceName;
        /**1、先判断节点存不存在，存在就删除，不存在就创建*/
        if (!ZkApiUtils.checkNodeExist(client,serviceNamePath)){
            logger.info("service node {} not existing,You don't have to create it again!",serviceNamePath);
            /**2、再创建服务节点（永久）*/
            ZkApiUtils.create(client,serviceNamePath);
            System.out.printf("Rpc-Server,成功注册服务节点：%s\n",serviceNamePath);
        }else{
            System.out.printf("Rpc-Server,服务节点：%s已存在\n",serviceNamePath);
        }
        // 3、为服务（接口）地址创建临时顺序节点（如果服务断开，临时节点在就删除）
        String serviceAddressPath = serviceNamePath+"/service-";
        String addressNode = ZkApiUtils.createEphemeralSequential(client, serviceAddressPath, serviceAddress);
        logger.info("create address node : {}" ,addressNode);
    }
}
