package cn.minglg.interview.common.config;

import cn.minglg.interview.common.properties.GlobalProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * ClassName:AsyncConfig
 * Package:cn.minglg.interview.common.config
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/1
 * @Version 1.0
 */
@RequiredArgsConstructor
@Configuration
public class AsyncConfig {
    private final GlobalProperties globalProperties;

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        int corePoolSize = globalProperties.getAsync().getCorePoolSize();
        int maxPoolSize = globalProperties.getAsync().getMaxPoolSize();
        int queueCapacity = globalProperties.getAsync().getQueueCapacity();
        String threadNamePrefix = globalProperties.getAsync().getThreadNamePrefix();
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix(threadNamePrefix);
        executor.initialize();
        return executor;
    }
}
