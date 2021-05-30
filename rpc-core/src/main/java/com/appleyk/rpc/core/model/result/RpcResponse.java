package com.appleyk.rpc.core.model.result;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * <p>封装RPC响应对象</p>
 *
 * @author appleyk
 * @version V.0.1.1
 * @blob https://blog.csdn.net/appleyk
 * @github https://github.com/kobeyk
 * @date created on 10:25 2021/5/18
 */
@Data
@Builder
public class RpcResponse {

    /**一个响应对应一个客户端请求（连接）标识*/
    private Long requestId;
    /**万一异常了，记录下*/
    private Exception e;
    /**响应结果*/
    private Object result;
    /**响应时间*/
    private Date date;

}
