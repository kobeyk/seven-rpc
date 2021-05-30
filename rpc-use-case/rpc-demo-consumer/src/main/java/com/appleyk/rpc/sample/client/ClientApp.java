package com.appleyk.rpc.sample.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * <p>RPC客户端用例启动类,项目启动后浏览器输入：http://localhost:8066/swagger-ui.html</p>
 *
 * @author appleyk
 * @version V.0.1.1
 * @blob https://blog.csdn.net/appleyk
 * @github https://github.com/kobeyk
 * @date created on 15:16 2021/3/16
 */
@SpringBootApplication
public class ClientApp extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ClientApp.class,args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ClientApp.class);
    }
}
