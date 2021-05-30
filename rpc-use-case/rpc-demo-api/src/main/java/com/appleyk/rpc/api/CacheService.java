package com.appleyk.rpc.api;

/**
 * 公共接口
 */
public interface CacheService {
    String save(String key,Object data);
    Object find(String key);
    void update(String key,Object data);
    void delete(String key);
}
