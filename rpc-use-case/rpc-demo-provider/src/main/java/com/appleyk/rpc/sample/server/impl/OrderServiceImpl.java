package com.appleyk.rpc.sample.server.impl;

import com.appleyk.rpc.api.OrderService;
import com.appleyk.rpc.model.Order;
import com.appleyk.rpc.common.util.IdUtils;
import com.appleyk.rpc.core.annotation.RpcService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>订单服务实现类</p>
 *
 * @author appleyk
 * @version V.0.1.1
 * @blob https://blog.csdn.net/appleyk
 * @github https://github.com/kobeyk
 * @date created on 9:30 2021/5/27
 */
@RpcService
//@Scope("prototype")
public class OrderServiceImpl implements OrderService {

    /*注意这个是单例bean中的变量，真实场景中不要这么做，对于有状态的bean还是要配置它的scope = "prototype"的*/
    private Map<Long,Order> orders = new ConcurrentHashMap<>();

    @Override
    public Order save(Order order) {
        long id = IdUtils.getId();
        order.setId(id);
        order.setcTime(new Date());
        orders.put(id,order);
        return order;
    }

    @Override
    public boolean delete(Long id) {
        orders.remove(id);
        return true;
    }

    @Override
    public Order update(Order order) {
        orders.put(order.getId(),order);
        return order;
    }

    @Override
    public List<Order> query() {
        return new ArrayList<>(orders.values());
    }
}
