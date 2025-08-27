package cn.minglg.ai.properties;

import lombok.Data;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

/**
 * ClassName:RoundLimitProperties
 * Package:cn.minglg.ai.properties
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/25
 * @Version 1.0
 */
@Data
@ConfigurationProperties(prefix = "interview.ai.advisor.round")
public class RoundLimitProperties {
    /**
     * 是否启用自动配置，默认不启用
     */
    private boolean enabled = false;
    /**
     * 最大轮次
     */
    private int maxRounds = 5;
    /**
     * 顺序，默认在ChatMemory之前100个位置
     */
    private int order = Advisor.DEFAULT_CHAT_MEMORY_PRECEDENCE_ORDER - 100;
    /**
     * 默认会话ID
     */
    private String defaultConversationId = ChatMemory.DEFAULT_CONVERSATION_ID;
    /**
     * 默认的taskType
     */
    private String defaultTaskTypeString = "GENERAL_CHAT";
    /**
     * 默认的调度器
     */
    private Scheduler scheduler = Schedulers.boundedElastic();
}
