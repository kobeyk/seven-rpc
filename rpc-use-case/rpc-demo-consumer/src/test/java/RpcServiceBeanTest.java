import com.appleyk.rpc.api.CacheService;
import com.appleyk.rpc.api.OrderService;
import com.appleyk.rpc.client.annotion.RpcAutowired;
import com.appleyk.rpc.common.util.JsonUtils;
import com.appleyk.rpc.model.Order;
import com.appleyk.rpc.sample.client.ClientApp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * <p>Rpc service bean 测试</p>
 *
 * @author appleyk
 * @version V.0.1.1
 * @blob https://blog.csdn.net/appleyk
 * @github https://github.com/kobeyk
 * @date created on 14:13 2021/5/26
 */
@SpringBootTest(classes = ClientApp.class)
public class RpcServiceBeanTest {

    /**第一种，使用我们自己定义的自动注入·注解，我们自己的可以指定bean的类型*/
    @RpcAutowired("mongodb")
    private CacheService cacheService1;

    /**第二种，使用Spring自带的自动注入·注解*/
    @Autowired
    private CacheService cacheService2;

    /**第二种，使用Spring自带的自动注入·注解*/
    @RpcAutowired
    private OrderService orderService;

    @Test
    public void cacheSave1(){
        String result = cacheService1.save("111", "222");
        System.out.println("====> "+result);
    }

    @Test
    public void cacheSave2(){
        String result = cacheService2.save("111", "222");
        System.out.println("====> "+result);
    }

    @Test
    public void orderSave(){
        Order order = orderService.save(new Order("华为手机", 4200.21, 500));
        System.out.println(JsonUtils.parserJson(order));
    }

    @Test
    public void orderQuery(){
        List<Order> orders = orderService.query();
        System.out.println("====> "+orders);
    }

}

