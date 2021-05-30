import com.appleyk.rpc.sample.client.ClientApp;
import com.appleyk.rpc.registry.ServiceDiscovery;
import com.appleyk.rpc.registry.ServiceRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * <p>zk服务注册和发现中心功能测试</p>
 *
 * @author appleyk
 * @version V.0.1.1
 * @blob https://blog.csdn.net/appleyk
 * @github https://github.com/kobeyk
 * @date created on 15:12 2021/3/16
 */
@SpringBootTest(classes = ClientApp.class)
public class ZkCenterTest {

    @Autowired
    private ServiceRegistry registry;

    @Autowired
    private ServiceDiscovery discovery;

    String serviceName = "com.appleyk.rpc.api.CacheService-mongodb";
    String serviceAddress = "localhost:8080";

    @Test
    public void registry(){
        registry.register(serviceName,serviceAddress);
    }

    @Test
    public void discover(){
        System.out.println(discovery.discover(serviceName));
    }

    /**
     * 服务ribbon测试，就是简单的随机策略
     */
    @Test
    public void choose(){
        List<Integer> children = new ArrayList<>();
        children.add(1);
        children.add(2);
        int answer = new Random().nextInt(children.size());
        int result = children.get(answer);
        System.out.println("帮你选择的结果是："+result);
    }
}
