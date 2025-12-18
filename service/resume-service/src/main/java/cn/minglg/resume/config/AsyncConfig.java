package cn.minglg.resume.config;

import cn.minglg.resume.properties.AsyncProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * ClassName:cn.minglg.resume.config.AsyncConfig
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
    private final AsyncProperties asyncProperties;

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        int corePoolSize = asyncProperties.getCorePoolSize();
        int maxPoolSize = asyncProperties.getMaxPoolSize();
        int queueCapacity = asyncProperties.getQueueCapacity();
        String threadNamePrefix = asyncProperties.getThreadNamePrefix();
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix(threadNamePrefix);
        executor.initialize();
        return executor;
    }
}
