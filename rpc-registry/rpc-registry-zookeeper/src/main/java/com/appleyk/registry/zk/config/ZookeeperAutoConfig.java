package com.appleyk.registry.zk.config;

import com.appleyk.registry.zk.impl.ZookeeperCenter;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * <p>zk客户端自动配置类</p>
 *
 * @author appleyk
 * @version V.0.1.1
 * @blob https://blog.csdn.net/appleyk
 * @github https://github.com/kobeyk
 * @date created on 12:48 2021/3/12
 */
@Configuration
@ComponentScan("com.appleyk.registry.zk")
@ConditionalOnClass({ZookeeperCenter.class}) //	当给定的类名在类路径上存在，则实例化当前Bean
@EnableConfigurationProperties(ZookeeperProperty.class)
public class ZookeeperAutoConfig {

    private final ZookeeperProperty property;

    public ZookeeperAutoConfig(ZookeeperProperty property){
        this.property = property;
    }

    @Bean(name = "client")
    @ConditionalOnMissingBean // 如果当前bean不存在的时候，则实例化当前bean
    public CuratorFramework curatorClient() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000, 10);
        CuratorFramework client =
                CuratorFrameworkFactory.builder()
                        .connectString(property.getHost()+":"+property.getPort())
                        .sessionTimeoutMs(property.getTimeout())
                        .connectionTimeoutMs(60 * 1000)
                        // 设置命名空间
                        .namespace(property.getNameSpace())
                        .retryPolicy(retryPolicy).build();
        client.start();
        System.out.println("========== ZkServer Starting ... ==========");
        return client;
    }

}
