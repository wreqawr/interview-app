package org.minglg.ai.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * ClassName:AiProperties
 * Package:cn.minglg.ai.properties
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/22
 * @Version 1.0
 */
@ConfigurationProperties(prefix = "interview.ai")
@Data
public class AiProperties {
    /**
     * 持久化上下文记忆的redis前缀
     */
    private String chatMemoryRedisKeyPrefix = "chat:history";
    /**
     * 会话保存时间（天）
     */
    private Integer chatMemoryRedisExpireDays = 30;

    /**
     * 单次对话上下文最长消息条数
     */
    private Integer maxChatMessages = 50;

    /**
     * 提示词模板渲染器使用的起始分隔符字符
     */
    private Character startDelimiterCharacter = '<';

    /**
     * 提示词模板渲染器使用的结束分隔符字符
     */
    private Character endDelimiterCharacter = '>';

    /**
     * 功能更强大的提示词模板渲染器使用的起始分隔符字符串
     */
    private String startDelimiterString = "#{";

    /**
     * 功能更强大的提示词模板渲染器使用的结束分隔符字符串
     */
    private String endDelimiterString = "}";

    /**
     * 聊天上下文存储仓库，可选：memory、redis、mongodb
     * 如果不填，默认基于内存存储
     */
    private String chatMemoryRepository = "memory";
}
