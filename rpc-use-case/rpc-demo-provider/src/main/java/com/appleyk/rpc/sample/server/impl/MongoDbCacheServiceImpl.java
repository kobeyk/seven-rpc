package com.appleyk.rpc.sample.server.impl;

import com.appleyk.rpc.api.CacheService;
import com.appleyk.rpc.core.annotation.RpcService;
import org.springframework.context.annotation.Primary;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>Rpc服务端，XXXX接口实现类 ==> MB模拟</p>
 *
 * @author appleyk
 * @version V.0.1.1
 * @blob https://blog.csdn.net/appleyk
 * @github https://github.com/kobeyk
 * @date created on 11:55 2021/5/18
 */
@Primary
@RpcService(name = "mongodb") //暴露服务
public class MongoDbCacheServiceImpl implements CacheService {

    private Map<String,Object> caches = new ConcurrentHashMap<>();

    @Override
    public String save(String key, Object data) {
        System.out.printf("MongoDb实现缓存#save，key={%s}\n",key);
        caches.put(key,data);
        return String.format("key:%s,value:%s",key,data);
    }

    @Override
    public Object find(String key) {
        System.out.printf("MongoDb实现缓存#find，key={%s}\n",key);
        return caches.getOrDefault(key,"None");
    }

    @Override
    public void update(String key, Object data) {
        System.out.printf("MongoDb实现缓存#update，key={%s}\n",key);
        caches.put(key,data);
    }

    @Override
    public void delete(String key) {
        System.out.printf("MongoDb实现缓存#delete，key={%s}\n",key);
        caches.remove(key);
    }
}
