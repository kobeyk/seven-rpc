package com.appleyk.rpc.api;

import com.appleyk.rpc.model.Order;

import java.util.List;

/**
 * <p>订单（接口）服务</p>
 *
 * @author appleyk
 * @version V.0.1.1
 * @blob https://blog.csdn.net/appleyk
 * @github https://github.com/kobeyk
 * @date created on 9:19 2021/5/27
 */
public interface OrderService {
    Order save(Order order);
    boolean delete(Long id);
    Order update(Order order);
    List<Order> query();
}
