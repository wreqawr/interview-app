package cn.minglg.interview.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * ClassName:AiProperties
 * Package:cn.minglg.interview.common.properties
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/11
 * @Version 1.0
 */
@ConfigurationProperties(prefix = "global.ai")
@Component
@Data
public class AiProperties {
    /**
     * 持久化上下文记忆的redis前缀
     */
    private String chatMemoryRedisKeyPrefix;
    /**
     * 会话保存时间（天）
     */
    private Long chatMemoryRedisExpireDays;

    /**
     * 单次对话上下文最长消息条数
     */
    private Integer maxChatMessages = 10;
}
