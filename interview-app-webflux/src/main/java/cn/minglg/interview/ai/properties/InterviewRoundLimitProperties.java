package cn.minglg.interview.ai.properties;

import lombok.Data;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

/**
 * ClassName:InterviewRoundLimitProperties
 * Package:cn.minglg.interview.ai.properties
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/28
 * @Version 1.0
 */
@ConfigurationProperties(prefix = "interview.ai.advisor.round")
@Component
@Data
public class InterviewRoundLimitProperties {
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
    private String defaultConversationId = "interview_round_limit_default";

    /**
     * 会话ID的key
     */
    private final String conversationIdKey = ChatMemory.CONVERSATION_ID;

    /**
     * 任务类型的key
     */
    private String taskTypeKey = "interview_round_limit_task_type";

    /**
     * 默认的taskType
     */
    private String defaultTaskTypeString = "GENERAL_CHAT";
    /**
     * 默认的调度器
     */
    private Scheduler scheduler = Schedulers.boundedElastic();
}
