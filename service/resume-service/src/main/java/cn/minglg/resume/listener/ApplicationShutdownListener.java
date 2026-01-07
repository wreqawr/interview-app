package cn.minglg.resume.listener;

import cn.minglg.commons.bloom.BloomFilter;
import cn.minglg.resume.constants.ResumeConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ClassName:ApplicationShutdownListener
 * Package:cn.minglg.resume.listener
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2026/1/7
 * @Version 1.0
 */
@RequiredArgsConstructor
@Component
public class ApplicationShutdownListener implements ApplicationListener<ContextClosedEvent> {
    private final List<BloomFilter<?>> bloomFilter;
    private final StringRedisTemplate redisTemplate;

    /**
     * 应用程序事件监听方法，处理上下文关闭事件
     * 在应用关闭时清理布隆过滤器和相关的Redis缓存数据
     *
     * @param event 上下文关闭事件对象，包含应用关闭的相关信息
     */
    @Override
    public void onApplicationEvent(@NonNull ContextClosedEvent event) {
        // 销毁所有布隆过滤器实例，释放内存资源
        bloomFilter.forEach(BloomFilter::destroyBloomFilter);
        // 删除Redis中以简历元数据前缀开头的所有缓存键
        deleteKeysByPrefix(ResumeConstants.RESUME_METADATA_REDIS_KEY_PREFIX);
    }


    /**
     * 删除所有以指定前缀开头的key
     *
     * @param prefix key前缀（例如：user:security:login）
     */
    private void deleteKeysByPrefix(String prefix) {
        // 构造匹配模式（注意：Redis的模式匹配需要包含冒号等特殊字符，直接使用前缀即可）
        ScanOptions scanOptions = ScanOptions.scanOptions().match(prefix + "*").count(100).build();

        // 使用游标迭代
        Cursor<String> cursor = redisTemplate.scan(scanOptions);
        while (cursor.hasNext()) {
            String key = cursor.next();
            // 逐个删除
            redisTemplate.delete(key);
        }
        cursor.close();
    }
}
