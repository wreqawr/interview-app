package cn.minglg.commons.async;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName:AsyncContext
 * Package:cn.minglg.commons.async
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/12/20
 * @Version 1.0
 */
@SuppressWarnings("unchecked")
public class AsyncContext {
    private final Map<String, Object> attributes = new HashMap<>();

    /**
     * 根据指定的键获取属性值
     *
     * @param <T> 属性值的泛型类型
     * @param key 要获取的属性对应的键
     * @return 返回与指定键关联的属性值，如果不存在则返回null
     */
    public <T> T getAttribute(String key) {
        return (T) attributes.get(key);
    }

    /**
     * 设置属性值
     *
     * @param key   属性的键名
     * @param value 属性的值
     * @param <T>   属性值的泛型类型
     */
    public <T> void setAttribute(String key, T value) {
        attributes.put(key, value);
    }

    /**
     * 清除此对象中的所有属性。
     * 此方法会移除当前对象中存储的所有属性信息，
     * 使属性集合变为空集合。
     */
    public void clear() {
        attributes.clear();
    }

}
