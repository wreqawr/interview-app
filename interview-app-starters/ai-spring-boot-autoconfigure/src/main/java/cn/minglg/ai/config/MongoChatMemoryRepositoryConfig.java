package cn.minglg.ai.config;

import cn.minglg.ai.context.UserContextProvider;
import cn.minglg.ai.repository.MongoChatMemoryRepository;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

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
    public ChatMemoryRepository mongoChatMemoryRepository(MongoTemplate mongoTemplate, UserContextProvider userContextProvider) {
        return new MongoChatMemoryRepository(mongoTemplate, userContextProvider);
    }

}
