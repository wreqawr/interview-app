package cn.minglg.commons.async;

import cn.minglg.commons.model.constants.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskDecorator;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * ClassName:cn.minglg.commons.async.AsyncConfig
 * Package:cn.minglg.interview.common.config
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/1
 * @Version 1.0
 */
@RequiredArgsConstructor
@Configuration
//@ConditionalOnProperty(name = "interview.async")
@Slf4j
public class AsyncConfig {
    /**
     * 用于在 RequestAttributes 中存储 Authorization token 的 key
     */
    private final AsyncProperties asyncProperties;

    @Bean(name = "taskExecutor")
    @Primary
    public Executor taskExecutor(TaskDecorator taskDecorator) {
        int corePoolSize = asyncProperties.getCorePoolSize();
        int maxPoolSize = asyncProperties.getMaxPoolSize();
        int queueCapacity = asyncProperties.getQueueCapacity();
        String threadNamePrefix = asyncProperties.getThreadNamePrefix();
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix(threadNamePrefix);
        // 设置 TaskDecorator，用于将 RequestAttributes 传递到异步线程
        executor.setTaskDecorator(taskDecorator);
        executor.initialize();
        return executor;
    }

    /**
     * 创建并配置TaskDecorator Bean，用于在线程池任务执行前后传递请求上下文信息
     * 该装饰器主要解决异步线程中无法获取主线程HttpServletRequest对象的问题，
     * 通过将主线程中的Authorization Token传递给子线程，确保异步任务能够正确访问认证信息
     *
     * @return TaskDecorator 任务装饰器实例
     */
    @Bean
    public TaskDecorator taskDecorator() {
        return new TaskDecorator() {
            /**
             * 装饰Runnable任务，在任务执行前后处理上下文信息的传递
             *
             * @param runnable 原始的任务执行逻辑，不能为空
             * @return 包装后的Runnable任务，确保在子线程中能够访问必要的上下文信息
             */
            @Override
            @NonNull
            public Runnable decorate(@NonNull Runnable runnable) {
                // 获取当前线程的RequestAttributes
                RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
                String authorizationToken = null;
                if (requestAttributes instanceof ServletRequestAttributes attributes) {
                    authorizationToken = attributes.getRequest().getHeader("Authorization");
                }
                log.info("从主线程获取到的Authorization token: {}", authorizationToken);
                AsyncContextHolder.setAttribute(Constants.AUTHORIZATION_TOKEN_KEY, authorizationToken);
                final String token = authorizationToken;

                // 返回包装后的任务，在子线程执行前设置上下文信息
                return () -> {
                    try {
                        log.info("为子线程设置Authorization token: {}", token);
                        AsyncContextHolder.setAttribute(Constants.AUTHORIZATION_TOKEN_KEY, token);
                        runnable.run();
                    } finally {
                        log.info("子线程执行完毕，清除Authorization token");
                        AsyncContextHolder.clearContext();
                    }
                };
            }
        };
    }

}
