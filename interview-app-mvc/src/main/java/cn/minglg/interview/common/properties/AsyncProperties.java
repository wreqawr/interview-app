package cn.minglg.interview.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * ClassName:AsyncProperties
 * Package:cn.minglg.interview.common.properties
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/1
 * @Version 1.0
 */
@ConfigurationProperties(prefix = "global.async")
@Component
@Data
public class AsyncProperties {
    /**
     * 核心线程数：线程池长期保持的线程数
     */
    private int corePoolSize;

    /**
     * 最大线程数：线程池允许创建的最大线程数
     */
    private int maxPoolSize;

    /**
     * 队列容量：当核心线程都在忙时，新任务进入队列等待，这里设置队列容量为100
     */
    private int queueCapacity;

    /**
     * 线程名前缀：线程名称的前缀，方便日志追踪（如：AsyncTask-1, AsyncTask-2...）
     */
    private String threadNamePrefix;
}
