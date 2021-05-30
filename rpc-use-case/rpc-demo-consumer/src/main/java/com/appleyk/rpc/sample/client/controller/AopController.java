package com.appleyk.rpc.sample.client.controller;

import com.appleyk.rpc.common.result.HttpResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * <p></p>
 *
 * @author appleyk
 * @version V.0.1.1
 * @blob https://blog.csdn.net/appleyk
 * @github https://github.com/kobeyk
 * @date created on 13:36 2021/5/27
 */
@CrossOrigin
@RestController
@RequestMapping("/rpc")
@Api(tags = "1、AOP统计接口执行时间用例测试")
public class AopController {
    @ApiOperation("AOP（面向切面编程）用例，主要为了测试玩，本质上和rpc框架无关！")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "姓名", defaultValue = "appleyk")
    })
    @GetMapping("/aop")
    public HttpResult aop(@RequestParam(value = "name",required = false,defaultValue = "appleyk") String name){
        return HttpResult.ok("hello,"+name);
    }
}
