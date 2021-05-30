package com.appleyk.rpc.sample.client.controller;

import com.appleyk.rpc.api.CacheService;
import com.appleyk.rpc.client.annotion.RpcAutowired;
import com.appleyk.rpc.common.result.HttpResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * <p>RPC服务消费端接口用例测试</p>
 *
 * @author appleyk
 * @version V.0.1.1
 * @blob https://blog.csdn.net/appleyk
 * @github https://github.com/kobeyk
 * @date created on 16:48 2021/5/19
 */
@CrossOrigin
@RestController
@RequestMapping("/rpc")
@Api(tags = "2、缓存API用例测试")
public class CacheController {

    @RpcAutowired("mongodb")
    private CacheService cacheService;


    /**
     * 缓存保存
     * @param key 键
     * @param val 值
     * @return 响应结果
     */
    @ApiOperation("缓存保存（RPC）用例，主要测试k-v的保存接口功能是否OK")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "key", value = "键", defaultValue = "作者"),
            @ApiImplicitParam(name = "val", value = "值", defaultValue = "appleyk")
    })
    @GetMapping("/cache/save")
    public HttpResult save(@RequestParam(value = "key",defaultValue = "作者") String key,
                           @RequestParam(value = "val",defaultValue = "appleyk") String val){
        String result = cacheService.save(key, val);
        return HttpResult.ok(result);
    }


    @ApiOperation("缓存清除（RPC）用例，主要测试k-v的清除接口功能是否OK")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "key", value = "键", defaultValue = "作者"),
            @ApiImplicitParam(name = "val", value = "值", defaultValue = "appleyk")
    })
    @DeleteMapping("/cache/delete")
    public HttpResult delete(@RequestParam(value = "key",defaultValue = "作者") String key){
        cacheService.delete(key);
        return HttpResult.ok("删除成功！");
    }


    @ApiOperation("缓存更新（RPC）用例，主要测试k-v的更新接口功能是否OK")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "key", value = "键", defaultValue = "作者"),
            @ApiImplicitParam(name = "val", value = "值", defaultValue = "appleyk")
    })
    @GetMapping("/cache/update")
    public HttpResult update(@RequestParam(value = "key",defaultValue = "作者") String key,
                             @RequestParam(value = "val",defaultValue = "appleyk") String val){
        cacheService.update(key, val);
        return HttpResult.ok("更新成功！");
    }

    /**
     * 缓存查询
     * @param key 键
     * @return 响应结果
     */
    @ApiOperation("缓存查询（RPC）用例，主要测试k-v的查询接口功能是否OK")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "key", value = "键", defaultValue = "作者")
    })
    @GetMapping("/cache/query")
    public HttpResult query(@RequestParam(value = "key",defaultValue = "作者") String key){
        Object result = cacheService.find(key);
        return HttpResult.ok(result);
    }


}
