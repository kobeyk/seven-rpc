import com.appleyk.rpc.sample.server.impl.MongoDbCacheServiceImpl;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Type;

public class ClassInterfacesTest {
    /**
     * 获取实现类直接实现的接口（包含泛型参数）数组
     * @param args
     */
    @Test
    public void main(String[] args) {
        MongoDbCacheServiceImpl mongodb = new MongoDbCacheServiceImpl();
        Type[] genericInterfaces = mongodb.getClass().getGenericInterfaces();
        System.out.println(genericInterfaces[0].getTypeName());
    }
}
