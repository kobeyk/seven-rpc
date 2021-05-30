package com.appleyk.registry.zk.util;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * <p>
 *     CuratorFramework 是Netflix公司开发的一款连接zookeeper服务的框架，
 *     提供了比较全面的功能，除了基础的节点的操作，节点的监听，还有集群的连接以及重试。
 * </p>
 *
 * @author appleyk
 * @version V.0.1.1
 * @blob https://blog.csdn.net/appleyk
 * @github https://github.com/kobeyk
 * @date created on 12:36 2021/3/16
 */
public class ZkApiUtils{

    /**
     * （默认）创建持久化节点,并附带内容
     * this will create the given ZNode with the given data
     */
    public static void create(CuratorFramework client, String path, byte[] payload) {
        try {
            if(checkNodeExist(client,path)){return;}
            client.create().forPath(path, payload);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * （默认）创建持久化节点
     * this will create the given ZNode with the given data
     */
    public static String create(CuratorFramework client,String path,String data) {
        try {
            if(checkNodeExist(client,path)){return path;}
            return client.create().forPath(path,data.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * （默认）创建持久化节点
     * this will create the given ZNode with the given data
     */
    public static void create(CuratorFramework client, String path) {
        try {
            if(checkNodeExist(client,path)){return;}
            client.create().forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * this will create the given EPHEMERAL ZNode with the given data
     */
    public static void createEphemeral(CuratorFramework client, String path, byte[] payload) {
        try {
            client.create().withMode(CreateMode.EPHEMERAL).forPath(path, payload);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建临时顺序节点(注意：临时节点下面不能再创建子节点)
     * this will create the given EPHEMERAL-SEQUENTIAL ZNode with
     * the given data using Curator protection.
     */
    public static String createEphemeralSequential(CuratorFramework client, String path, String data) {
        try {
            return client.create().withProtection().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(path,data.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 创建临时顺序节点
     * this will create the given EPHEMERAL-SEQUENTIAL ZNode with
     * the given data using Curator protection.
     */
    public static String createEphemeralSequential(CuratorFramework client, String path, byte[] payload) {
        try {
            return client.create().withProtection().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(path, payload);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * set data for the given node
     */
    public static void setData(CuratorFramework client, String path, byte[] payload) {
        try {
            client.setData().forPath(path, payload);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * delete the given node
     */
    public static void delete(CuratorFramework client, String path) {
        try {
            client.delete().forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保证节点一定被删除
     * delete the given node and guarantee that it completes
     */
    public static void guaranteedDelete(CuratorFramework client, String path) {
        try {
            client.delete().guaranteed().forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get children and set a watcher on the node. The watcher notification
     */
    public static List<String> watchedGetChildren(CuratorFramework client, String path) {
        try {
            return client.getChildren().watched().forPath(path);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Get children from the path （获取path上的孩子节点）
     */
    public static List<String> getPathChildren(CuratorFramework client, String path) {
        try {
            return client.getChildren().forPath(path);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     *  获取节点的值
     */
    public static String getNodeValue(CuratorFramework client, String path) {
        try {
            byte[] value = client.getData().forPath(path);
            return value == null ? null : new String(value);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 判断节点是否存在
     */
    public static boolean checkNodeExist(CuratorFramework client, String path) {
        boolean flag = true;
        try {
            Stat stat = client.checkExists().forPath(path);
            // Stat就是对zNode所有属性的一个映射， stat=null表示节点不存在！
            if (stat == null) {
                flag = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }
}
