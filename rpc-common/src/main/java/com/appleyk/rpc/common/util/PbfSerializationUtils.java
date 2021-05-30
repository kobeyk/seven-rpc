package com.appleyk.rpc.common.util;

import com.appleyk.rpc.core.model.result.RpcRequest;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>基于protostuff进行对象的序列化和反序列化</p>
 * 需要注意的地方：
 *     protostuff的序列化过程是按照数据模型中的字段顺序依次序列化字段的值，
 * 反序列化时会按顺序解析数据然后赋给对应的字段，所以数据模型要遵循以下原则，避免数据错乱：
 *
 * 1、数据模型中的字段一旦使用不可再删除，除非先清除存储的所有序列化数据(但是在线上项目中不推荐这么干)
 * 2、数据模型中新增字段必须放在最后面!!!
 * 3、如果是在内部对象新增字段，直接在内部对象数据模型中最后一个字段的后面增加新字段即可
 * 4、protobuff只能序列化pojo类，不能直接序列化List 或者Map,如果要序列化list或者map的话，需要用一个wrapper类包装一下
 * 5、最后一条，在rpc框架中，其功能是够用了，实在不行，我用proto文件自己编译好吧
 */
public class PbfSerializationUtils {

    private static Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<>();
    private static Objenesis objenesis = new ObjenesisStd(true);
    private PbfSerializationUtils() {}

    /**
     * 序列化（对象 -> 字节数组）
     */
    @SuppressWarnings("unchecked")
    public static <T> byte[] serialize(T obj) {
        Class<T> cls = (Class<T>) obj.getClass();
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            Schema<T> schema = getSchema(cls);
            return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            buffer.clear();
        }
    }

    /**
     * 反序列化（字节数组 -> 对象）
     */
    public static <T> T deserialize(byte[] data, Class<T> cls) {
        try {
            T message = objenesis.newInstance(cls);
            Schema<T> schema = getSchema(cls);
            ProtostuffIOUtil.mergeFrom(data, message, schema);
            return message;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private static <T> Schema<T> getSchema(Class<T> cls) {
        Schema<T> schema = (Schema<T>) cachedSchema.get(cls);
        if (schema == null) {
            schema = RuntimeSchema.createFrom(cls);
            cachedSchema.put(cls, schema);
        }
        return schema;
    }

    public static void main(String[] args) {
        /*pbf序列化 和 json序列化  数据"体积"对比*/
        RpcRequest request = RpcRequest.builder()
                .methodName("hello")
                .interfaceName("HelloService")
                .requestId(IdUtils.getId())
                .parameters(new Object[]{"111", 22222, 2.0}).build();
        byte[] serialize = PbfSerializationUtils.serialize(request);
        System.out.println(serialize.length);
        String json = JsonUtils.parserJson(request);
        assert json != null;
        System.out.println(json.length());
        RpcRequest res = PbfSerializationUtils.deserialize(serialize,RpcRequest.class);
        System.out.println(res.getMethodName());
    }
}
