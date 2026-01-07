package cn.minglg.commons.bloom;

import org.redisson.api.RBloomFilter;

/**
 * ClassName:BloomFilter
 * Package:cn.minglg.commons.bloom
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2026/1/6
 * @Version 1.0
 */

public interface BloomFilter<T> {

    /**
     * 获取布隆过滤器实例
     *
     * @return 返回RBloomFilter类型的布隆过滤器实例
     */
    RBloomFilter<T> getBloomFilter();

    /**
     * 初始化布隆过滤器的生命周期回调方法
     * 在Spring容器完成依赖注入后自动调用此方法来初始化布隆过滤器
     */
    void initBloomFilter();

    /**
     * 判断指定值是否可能存在于此数据结构中
     * 该方法使用布隆过滤器进行存在性检查，可能存在误判（假阳性），但不会出现假阴性
     *
     * @param value 待检查的值
     * @return 如果布隆过滤器认为该值可能存在则返回true，否则返回false
     * 注意：返回false表示值一定不存在，返回true表示值可能存在（存在误判可能性）
     */
    default boolean maybeExist(T value) {
        return getBloomFilter().contains(value);
    }


    /**
     * 向布隆过滤器中添加值
     * 该方法将指定值添加到布隆过滤器中，用于后续的存在性检查
     *
     * @param value 待添加的值
     */
    default void addValue(T value) {
        getBloomFilter().add(value);
    }

    /**
     * 销毁布隆过滤器
     * 该方法在对象销毁前被调用，负责清理布隆过滤器资源
     * 通过调用布隆过滤器的delete方法来释放相关资源
     *
     */
    default void destroyBloomFilter() {
        getBloomFilter().delete();
    }

}
