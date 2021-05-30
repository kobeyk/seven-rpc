package com.appleyk.rpc.server.autoconfigure;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * <p>rpc-server's beans 自动装配</p>
 *
 * @author appleyk
 * @version V.0.1.1
 * @blob https://blog.csdn.net/appleyk
 * @date created on  下午10:40 2021/5/19
 */
@Configuration
@ComponentScan("com.appleyk.rpc.server")
public class RpcServerAutoConfigure {
    public RpcServerAutoConfigure() {
        System.out.println("=== RPC-SERVER complete automatic assembly ！===");
    }
}
