package org.minglg.ai.config;

import org.minglg.ai.context.UserContextProvider;
import org.minglg.ai.repository.webflux.ReactiveMongoChatMemoryRepository;
import org.minglg.ai.repository.webmvc.MongoChatMemoryRepository;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

/**
 * ClassName:MongoChatMemoryRepositoryConfig
 * Package:cn.minglg.ai.config
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/23
 * @Version 1.0
 */
@ConditionalOnClass(MongoTemplate.class)
@ConditionalOnProperty(
        name = "interview.ai.chat-memory-repository",
        havingValue = "mongodb"
)
@Configuration
public class MongoChatMemoryRepositoryConfig {
    /**
     * 创建MongoDB聊天记忆仓库Bean
     *
     * @param mongoTemplate       MongoDB模板对象，用于执行MongoDB操作
     * @param userContextProvider 用户上下文提供者，用于获取用户相关信息
     * @return 返回配置好的MongoChatMemoryRepository实例
     */
    @Bean("mongoChatMemoryRepository")
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public ChatMemoryRepository mongoChatMemoryRepository(MongoTemplate mongoTemplate, UserContextProvider userContextProvider) {
        return new MongoChatMemoryRepository(mongoTemplate, userContextProvider);
    }

    /**
     * 创建响应式MongoDB聊天记忆仓库Bean
     * 该方法仅在响应式Web应用环境下生效，用于创建基于MongoDB的聊天记忆存储实现
     *
     * @param mongoTemplate       响应式MongoDB模板，用于执行数据库操作
     * @param userContextProvider 用户上下文提供者，用于获取当前用户信息
     * @return 响应式MongoDB聊天记忆仓库实例
     */
    @Bean("reactiveMongoChatMemoryRepository")
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    public ChatMemoryRepository reactiveMongoChatMemoryRepository(ReactiveMongoTemplate mongoTemplate, UserContextProvider userContextProvider) {
        return new ReactiveMongoChatMemoryRepository(mongoTemplate, userContextProvider);
    }


}
