package com.appleyk.rpc.client.annotion.meta;

import lombok.Builder;
import lombok.Data;

/**
 * <p>rpc service bean 注入元数据</p>
 *
 * @author appleyk
 * @version V.0.1.1
 * @blob https://blog.csdn.net/appleyk
 * @github https://github.com/kobeyk
 * @date created on 14:19 2021/5/26
 */
@Data
@Builder
public class RpcServiceAnnotationMetaData {
    /**bean的类型*/
    private String type;
    /**bean的版本*/
    private String version;
    /**bean的（服务）组*/
    private String group;
}
