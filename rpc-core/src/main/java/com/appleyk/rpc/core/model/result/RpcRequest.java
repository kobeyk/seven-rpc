package com.appleyk.rpc.core.model.result;

import lombok.Builder;
import lombok.Data;

/**
 * <p>封装RPC请求对象</p>
 *
 * @author appleyk
 * @version V.0.1.1
 * @blob https://blog.csdn.net/appleyk
 * @github https://github.com/kobeyk
 * @date created on 10:25 2021/5/18
 */
@Data
@Builder
public class RpcRequest {
    /**请求唯一标识*/
    private Long requestId;
    /**接口名称*/
    private String interfaceName;
    /**接口实现类的类型（接口的多态：如一个接口有多个实现类，则要标注下实现类的类型，类型要唯一）*/
    private String type;
    /**需要（rc，remote call)调用的方法名称*/
    private String methodName;
    /**方法参数类型数组*/
    private Class<?>[] parameterTypes;
    /**方法参数值数组*/
    private Object[] parameters;
}
