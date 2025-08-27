package cn.minglg.ai.autoconfig;

import cn.minglg.ai.advisors.RoundLimitAdvisor;
import cn.minglg.ai.advisors.RoundLimitRepository;
import cn.minglg.ai.properties.RoundLimitProperties;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * ClassName:RoundLimitAutoConfiguration
 * Package:cn.minglg.ai.autoconfig
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/27
 * @Version 1.0
 */
@EnableConfigurationProperties(RoundLimitProperties.class)
@ConditionalOnProperty(name = "interview.ai.advisor.round.enabled", havingValue = "true")
@AutoConfiguration
public class RoundLimitAutoConfiguration {
    /**
     * 创建RoundLimitAdvisor Bean实例
     * 该方法用于创建RoundLimitAdvisor的Bean实例，用于处理对话轮次限制的切面逻辑。
     * 只有在Spring容器中不存在RoundLimitAdvisor类型的Bean时才会创建此Bean，
     * 并且需要ChatMemory、RoundLimitProperties和RoundLimitRepository三个Bean都存在时才会生效。
     *
     * @param chatMemory           聊天内存管理器，用于管理对话上下文
     * @param roundLimitProperties 轮次限制配置属性，包含限制相关的配置信息
     * @param roundLimitRepository 轮次限制数据仓库，用于持久化和查询限制数据
     * @return RoundLimitAdvisor实例，用于处理对话轮次限制的切面 advisor
     */
    @Bean
    @ConditionalOnMissingBean(RoundLimitAdvisor.class)
    public RoundLimitAdvisor roundLimitAdvisor(ChatMemory chatMemory,
                                               RoundLimitProperties roundLimitProperties,
                                               RoundLimitRepository roundLimitRepository) {
        return new RoundLimitAdvisor(chatMemory, roundLimitProperties, roundLimitRepository);
    }
}
