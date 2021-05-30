package com.appleyk.rpc.sample.client.controller;

import com.appleyk.rpc.api.OrderService;
import com.appleyk.rpc.client.annotion.RpcAutowired;
import com.appleyk.rpc.common.result.HttpResult;
import com.appleyk.rpc.model.Order;
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
 * @date created on 13:18 2021/5/27
 */
@CrossOrigin
@RestController
@RequestMapping("/rpc")
@Api(tags = "3、订单API用例测试")
public class OrderController {

    @RpcAutowired
    private OrderService orderService;

    @ApiOperation("订单保存（RPC）用例，主要测试订单数据存储接口功能是否OK")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "order", value = "订单")
    })
    /**
       {
        "name":"华为手机",
        "price":4002.5,
        "amount":500
       }
     */
    @PostMapping("/order/save")
    public HttpResult save(@RequestBody Order order){
        Order result = orderService.save(order);
        return HttpResult.ok(result);
    }

    @ApiOperation("订单删除（RPC）用例，主要测试订单数据删除接口功能是否OK")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "订单唯一标识")
    })
    @DeleteMapping("/order/delete/{id}")
    public HttpResult delete(@PathVariable(name = "id") Long id){
        return HttpResult.ok(orderService.delete(id));
    }

    @ApiOperation("订单更新（RPC）用例，主要测试订单数据更新接口功能是否OK")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "order", value = "订单")
    })
    @PostMapping("/order/update")
    public HttpResult update(@RequestBody Order order){
        Order result = orderService.update(order);
        return HttpResult.ok(result);
    }

    @ApiOperation("订单查询（RPC）用例，主要测试订单数据查询接口功能是否OK")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "order", value = "订单")
    })
    @GetMapping("/order/query")
    public HttpResult query(){
        return HttpResult.ok(orderService.query());
    }

}
