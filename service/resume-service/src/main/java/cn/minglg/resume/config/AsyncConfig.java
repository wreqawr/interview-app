package cn.minglg.resume.config;

import cn.minglg.resume.properties.AsyncProperties;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.Map;
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
    /**
     * 用于在 RequestAttributes 中存储 Authorization token 的 key
     */
    public static final String AUTHORIZATION_TOKEN_KEY = "async.authorization.token";

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
        // 设置 TaskDecorator，用于将 RequestAttributes 传递到异步线程
        executor.setTaskDecorator(new RequestContextTaskDecorator());
        executor.initialize();
        return executor;
    }

    /**
     * 任务装饰器，用于将当前线程的 RequestAttributes 传递到异步执行线程
     */
    private static class RequestContextTaskDecorator implements TaskDecorator {
        /**
         * 装饰Runnable任务，在异步执行时保持请求上下文信息
         *
         * @param runnable 原始的可运行任务，不能为空
         * @return 包装后的可运行任务，确保在异步执行时能够访问到请求上下文，且不会产生内存泄漏
         */
        @NotNull
        @Override
        public Runnable decorate(@NotNull Runnable runnable) {
            // 获取当前线程的RequestAttributes
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            String authorizationToken = null;

            // 在原始请求线程中提取Authorization token，避免在异步线程中访问已回收的HttpServletRequest
            if (requestAttributes instanceof ServletRequestAttributes attributes) {
                try {
                    authorizationToken = attributes.getRequest().getHeader("Authorization");
                } catch (Exception e) {
                    // 如果无法获取，忽略异常，后续会在FeignRequestInterceptor 中处理
                }
            }

            // 保存token的最终值（用于lambda表达式）
            final String token = authorizationToken;

            return () -> {
                try {
                    // 在异步线程中设置RequestAttributes
                    if (token != null) {
                        // 创建一个新的RequestAttributes来存储token，避免使用已回收的ServletRequestAttributes
                        SimpleRequestAttributes simpleRequestAttributes = new SimpleRequestAttributes();
                        simpleRequestAttributes.setAttribute(
                                AUTHORIZATION_TOKEN_KEY,
                                token,
                                RequestAttributes.SCOPE_REQUEST
                        );
                        RequestContextHolder.setRequestAttributes(simpleRequestAttributes, true);
                    }
                    // 注意：即使没有token，也不传递原始的ServletRequestAttributes，
                    // 因为它依赖于已回收的HttpServletRequest，会在异步线程中抛出异常
                    // 执行原始任务
                    runnable.run();
                } finally {
                    // 清理 RequestAttributes，避免内存泄漏
                    RequestContextHolder.resetRequestAttributes();
                }
            };
        }

    }

    /**
     * 简单的RequestAttributes实现，用于在异步线程中存储属性
     * 不依赖于HttpServletRequest，避免在异步线程中访问已回收的请求对象
     */
    private static class SimpleRequestAttributes implements RequestAttributes {
        private final Map<String, Object> requestAttributes = new HashMap<>();
        private final Map<String, Object> sessionAttributes = new HashMap<>();

        @Override
        public Object getAttribute(@NotNull String name, int scope) {
            return switch (scope) {
                case SCOPE_REQUEST -> requestAttributes.get(name);
                case SCOPE_SESSION -> sessionAttributes.get(name);
                default -> null;
            };
        }

        @Override
        public void setAttribute(@NotNull String name, @NotNull Object value, int scope) {
            switch (scope) {
                case SCOPE_REQUEST -> requestAttributes.put(name, value);
                case SCOPE_SESSION -> sessionAttributes.put(name, value);
            }
        }

        @Override
        public void removeAttribute(@NotNull String name, int scope) {
            switch (scope) {
                case SCOPE_REQUEST -> requestAttributes.remove(name);
                case SCOPE_SESSION -> sessionAttributes.remove(name);
            }
        }

        @NotNull
        @Override
        public String[] getAttributeNames(int scope) {
            return switch (scope) {
                case SCOPE_REQUEST -> requestAttributes.keySet().toArray(new String[0]);
                case SCOPE_SESSION -> sessionAttributes.keySet().toArray(new String[0]);
                default -> new String[0];
            };
        }

        @Override
        public void registerDestructionCallback(@NotNull String name, @NotNull Runnable callback, int scope) {
            // 简单实现，不处理销毁回调
        }

        @Override
        public Object resolveReference(@NotNull String key) {
            return null;
        }

        @NotNull
        @Override
        public String getSessionId() {
            return "";
        }

        @NotNull
        @Override
        public Object getSessionMutex() {
            return this;
        }
    }
}
