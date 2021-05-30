package com.appleyk.registry.zk.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

@Validated // 属性类上必须加这个注解，开启验证，不加的话，不会对属性字段的值进行校验
@ConfigurationProperties(prefix = "seven.rpc.zookeeper")
public class ZookeeperProperty {

    /*ZK命名空间，就是默认根节点*/
    private String nameSpace = "seven-rpc-registry";
    @NotBlank(message = "ZK服务地址不能为空！")
    private String host;
    @Max(value = 65536)
    private Integer port = 2181;
    /*（临时节点）会话过期时间*/
    private Integer timeout;

    public ZookeeperProperty(){}

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }
}
