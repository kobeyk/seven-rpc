package com.appleyk.rpc.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;

import java.io.IOException;
import java.util.*;

/**
 * JSON正反序列化工具类
 */
public class JsonUtils {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public JsonUtils() {
    }

    public static <T> String parserJson(T target) {
        if (GeneralUtils.isEmpty(target)) {
            return null;
        } else {
            try {
                return MAPPER.writeValueAsString(target);
            } catch (JsonProcessingException var2) {
                var2.printStackTrace();
                LoggerUtils.error("json序列化失败, " + var2.getMessage());
                return null;
            }
        }
    }

    public static <T> T parserBean(String json, Class<T> clazz) {
        if (GeneralUtils.isEmpty(json)) {
            return null;
        } else {
            try {
                return MAPPER.readValue(json, clazz);
            } catch (Exception var3) {
                var3.printStackTrace();
                LoggerUtils.error("json反序列化失败, " + var3.getMessage());
                return null;
            }
        }
    }

    public static <T> T[] parserArray(String json, Class<T> clazz) {
        if (GeneralUtils.isEmpty(json)) {
            return null;
        } else {
            ArrayType arrayType = MAPPER.getTypeFactory().constructArrayType(clazz);
            try {
                return (T[])MAPPER.readValue(json, arrayType);
            } catch (Exception var4) {
                var4.printStackTrace();
                LoggerUtils.error("json反序列化失败, " + var4.getMessage());
                return null;
            }
        }
    }

    public static <T> List<T> parserList(String json, CollectionType listType) {
        if (GeneralUtils.isEmpty(json)) {
            return null;
        } else {
            try {
                return (List)MAPPER.readValue(json, listType);
            } catch (Exception var3) {
                var3.printStackTrace();
                LoggerUtils.error("json反序列化失败, " + var3.getMessage());
                return Collections.emptyList();
            }
        }
    }

    public static <Z extends List, T> List<T> parserList(String json, Class<Z> parserClazz, Class<T> clazz) {
        CollectionType listType = MAPPER.getTypeFactory().constructCollectionType(parserClazz, clazz);
        return parserList(json, listType);
    }

    public static <T> List<T> parserList(String json, Class<T> clazz) {
        return parserList(json, List.class, clazz);
    }

    public static <T> Set<T> parserSet(String json, CollectionType setType) {
        if (GeneralUtils.isEmpty(json)) {
            return null;
        } else {
            try {
                return (Set)MAPPER.readValue(json, setType);
            } catch (Exception var3) {
                var3.printStackTrace();
                LoggerUtils.error("json反序列化失败, " + var3.getMessage());
                return Collections.emptySet();
            }
        }
    }

    public static <Z extends Set, T> Set<T> parserSet(String json, Class<Z> parserClazz, Class<T> clazz) {
        CollectionType setType = MAPPER.getTypeFactory().constructCollectionType(parserClazz, clazz);
        return parserSet(json, setType);
    }

    public static <T> Set<T> parserSet(String json, Class<T> clazz) {
        return parserSet(json, Set.class, clazz);
    }

    public static <T, K> Map<T, K> parserMap(String json, MapType mapType) {
        if (GeneralUtils.isEmpty(json)) {
            return null;
        } else {
            try {
                return (Map)MAPPER.readValue(json, mapType);
            } catch (Exception var3) {
                var3.printStackTrace();
                LoggerUtils.error("json反序列化失败, " + var3.getMessage());
                return Collections.emptyMap();
            }
        }
    }

    public static <Z extends Map, T, K> Map<T, K> parserMap(String json, Class<Z> parserClazz, Class<T> keyClazz, Class<K> valueClazz) {
        MapType mapType = MAPPER.getTypeFactory().constructMapType(parserClazz, keyClazz, valueClazz);
        return parserMap(json, mapType);
    }

    public static <T, K> Map<T, K> parserMap(String json, Class<T> keyClazz, Class<K> valueClazz) {
        return parserMap(json, Map.class, keyClazz, valueClazz);
    }

    public static <K, T> Hashtable<K, T> parserTable(String json, Class<K> keyClazz, Class<T> valueClazz) {
        MapType mapType = MAPPER.getTypeFactory().constructMapType(Hashtable.class, keyClazz, valueClazz);
        if (GeneralUtils.isEmpty(json)) {
            return null;
        } else {
            try {
                return (Hashtable)MAPPER.readValue(json, mapType);
            } catch (IOException var5) {
                var5.printStackTrace();
                LoggerUtils.error("json反序列化失败, " + var5.getMessage());
                return new Hashtable();
            }
        }
    }

    public static <H extends List, Y extends Map, T, K> Map<T, List<K>> parserMapList(String json, Class<H> parserListClazz, Class<Y> parserMapClazz, Class<T> keyClazz, Class<K> valueClazz) {
        CollectionType collectionType = MAPPER.getTypeFactory().constructCollectionType(parserListClazz, valueClazz);
        MapType mapType = MAPPER.getTypeFactory().constructMapType(parserMapClazz, keyClazz, collectionType.getRawClass());
        return parserMap(json, mapType);
    }

    public static <T, K> Map<T, List<K>> parserMapList(String json, Class<T> keyClazz, Class<K> valueClazz) {
        return parserMapList(json, List.class, Map.class, keyClazz, valueClazz);
    }

    public static <H extends Map, Y extends Map, Z, T, K> Map<Z, Map<T, K>> parserMapMap(String json, Class<H> wrapMapClazz, Class<Y> contentMapClazz, Class<Z> wrapKeyClazz, Class<T> contentKeyClazz, Class<K> contentValueClazz) {
        MapType contentType = MAPPER.getTypeFactory().constructMapType(contentMapClazz, contentKeyClazz, contentValueClazz);
        MapType mapType = MAPPER.getTypeFactory().constructMapType(wrapMapClazz, wrapKeyClazz, contentType.getRawClass());
        return parserMap(json, mapType);
    }

    public static <Z, T, K> Map<Z, Map<T, K>> parserMapMap(String json, Class<Z> wrapKeyClazz, Class<T> contentKeyClazz, Class<K> contentValueClazz) {
        return parserMapMap(json, Map.class, Map.class, wrapKeyClazz, contentKeyClazz, contentValueClazz);
    }
}
