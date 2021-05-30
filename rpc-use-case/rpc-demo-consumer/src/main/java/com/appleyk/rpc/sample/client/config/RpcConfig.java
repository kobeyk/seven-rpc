package com.appleyk.rpc.sample.client.config;

import com.appleyk.rpc.client.annotion.EnableSeven;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * <p>
 *     Rpc"代理"配置，主要是扫描公共接口包，
 *     改写spring扫描bean定义的条件，然后借助FactoryBean实现动态代理
 * </p>
 *
 * @author appleyk
 * @version V.0.1.1
 * @blob https://blog.csdn.net/appleyk
 * @github https://github.com/kobeyk
 * @date created on 17:34 2021/5/19
 */
@Configuration
//开启rpc功能（指定扫码的基础包，以对包下面的service bean进行一个rpc方式的代理）
@EnableSeven(scanBasePackages = "com.appleyk.rpc.api")
@EnableSwagger2
public class RpcConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .pathMapping("/")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.appleyk.rpc.sample.client.controller"))
                .paths(PathSelectors.any())
                .build().apiInfo(new ApiInfoBuilder()
                        .title("Rpc（远程过程调用）API文档")
                        .description("基于SpringBoot+Netty两大框架实现的一个简易版的个人RPC框架")
                        .version("v0.1.1")
                        .contact(new Contact("Appleyk","https://blog.csdn.net/Appleyk",
                                "yukun24@126.com"))
                        .build());
    }

}
