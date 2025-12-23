package org.minglg.ai.config;

import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ClassName:InMemoryChatMemoryRepositoryConfig
 * Package:cn.minglg.ai.config
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/23
 * @Version 1.0
 */
@Configuration
@ConditionalOnProperty(
        name = "interview.ai.chat-memory-repository",
        havingValue = "memory",
        matchIfMissing = true
)
public class InMemoryChatMemoryRepositoryConfig {

    /**
     * 创建默认的聊天记忆存储库Bean
     * 当没有其他ChatMemoryRepository实现时，使用内存存储作为后备方案
     *
     * @return ChatMemoryRepository 默认的内存存储实现
     */
    @Bean("inMemoryChatMemoryRepository")
    public ChatMemoryRepository inMemoryChatMemoryRepository() {
        return new InMemoryChatMemoryRepository();
    }

}
