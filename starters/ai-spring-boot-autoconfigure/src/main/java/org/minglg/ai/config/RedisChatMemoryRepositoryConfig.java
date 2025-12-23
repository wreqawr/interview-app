package org.minglg.ai.config;

import org.minglg.ai.context.UserContextProvider;
import org.minglg.ai.properties.AiProperties;
import org.minglg.ai.repository.webflux.ReactiveRedisChatMemoryRepository;
import org.minglg.ai.repository.webmvc.RedisChatMemoryRepository;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * ClassName:RedisChatMemoryRepositoryConfig
 * Package:cn.minglg.ai.config
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/23
 * @Version 1.0
 */
@Configuration
@ConditionalOnClass({StringRedisTemplate.class})
@ConditionalOnProperty(
        name = "interview.ai.chat-memory-repository",
        havingValue = "redis"
)
public class RedisChatMemoryRepositoryConfig {
    /**
     * 创建Redis聊天记忆仓库Bean
     *
     * @param redisTemplate       Redis模板，用于与Redis进行数据交互
     * @param aiProperties        AI配置属性，包含AI相关的配置信息
     * @param userContextProvider 用户上下文提供者，用于获取用户相关信息
     * @return Redis聊天记忆仓库实例
     */
    @Bean("redisChatMemoryRepository")
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public ChatMemoryRepository redisChatMemoryRepository(StringRedisTemplate redisTemplate, AiProperties aiProperties, UserContextProvider userContextProvider) {
        return new RedisChatMemoryRepository(redisTemplate, aiProperties, userContextProvider);
    }

    @Bean("reactiveRedisChatMemoryRepository")
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    public ChatMemoryRepository reactiveRedisChatMemoryRepository(ReactiveStringRedisTemplate redisTemplate, AiProperties aiProperties, UserContextProvider userContextProvider) {
        return new ReactiveRedisChatMemoryRepository(redisTemplate, aiProperties, userContextProvider);
    }
}
