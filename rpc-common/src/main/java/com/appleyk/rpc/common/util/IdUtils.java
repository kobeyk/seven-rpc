package com.appleyk.rpc.common.util;

/**
 * <p>ID工具类，简单起见，就返回时间戳吧，不想搞什么雪花分布式ID了</p>
 *
 * @author appleyk
 * @version V.0.1.1
 * @blob https://blog.csdn.net/appleyk
 * @date created on  下午10:10 2021/5/22
 */
public class IdUtils {
    public static long getId(){
        return System.currentTimeMillis();
    }
}
