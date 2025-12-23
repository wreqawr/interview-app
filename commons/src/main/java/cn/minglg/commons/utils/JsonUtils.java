package cn.minglg.commons.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.List;
import java.util.Map;

/**
 * ClassName:JsonUtils
 * Package:cn.minglg.authentication.utils
 * Description:json工具类
 *
 * @Author kfzx-minglg
 * @Create 2025/8/15
 * @Version 1.0
 */
public class JsonUtils {
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        // 自动注册所有模块（包括 JavaTimeModule）
        OBJECT_MAPPER.findAndRegisterModules();
        // 时间以字符串输出
        OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // 允许空bean（如代理类 handler 等）
        OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // 反序列化忽略未知属性
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * 将对象序列化为JSON字符串
     *
     * @param obj 需要序列化的对象
     * @return 序列化后的JSON字符串
     * @throws RuntimeException 当对象序列化失败时抛出运行时异常
     */
    public static String toJsonStr(Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("对象序列化成json字符串失败: " + e.getOriginalMessage(), e);
        }
    }

    /**
     * 将JSON字符串转换为指定类型的对象
     *
     * @param json  要转换的JSON字符串
     * @param clazz 目标对象的Class类型
     * @return 转换后的对象实例
     * @throws RuntimeException 当JSON反序列化失败时抛出运行时异常
     */
    public static <T> T toBean(String json, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("json字符串反序列化成对象失败: " + e.getOriginalMessage(), e);
        }
    }

    /**
     * 将JSON字符串转换为指定类型的List集合
     *
     * @param json  待转换的JSON字符串
     * @param clazz List中元素的类型Class对象
     * @return 转换后的List集合
     * @throws RuntimeException 当JSON反序列化失败时抛出运行时异常
     */
    public static <T> List<T> toList(String json, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(json, OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("json字符串反序列化成List失败: " + e.getOriginalMessage(), e);
        }
    }

    /**
     * 将JSON字符串转换为指定键值类型的Map对象
     *
     * @param json       JSON字符串
     * @param keyClass   Map键的类型Class对象
     * @param valueClass Map值的类型Class对象
     * @return 转换后的Map对象
     * @throws RuntimeException 当JSON反序列化失败时抛出运行时异常
     */
    public static <K, V> Map<K, V> toMap(String json, Class<K> keyClass, Class<V> valueClass) {
        try {
            return OBJECT_MAPPER.readValue(json, OBJECT_MAPPER.getTypeFactory().constructMapType(Map.class, keyClass, valueClass));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("json字符串反序列化成Map失败: " + e.getOriginalMessage(), e);
        }
    }
}
