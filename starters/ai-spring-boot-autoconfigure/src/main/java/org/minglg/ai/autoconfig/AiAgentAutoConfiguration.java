package org.minglg.ai.autoconfig;

import org.minglg.ai.agent.properties.AiAgentProperties;
import org.minglg.ai.agent.service.AiAgentService;
import org.minglg.ai.agent.service.impl.AiAgentServiceImpl;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * ClassName:AiAgentAutoConfiguration
 * Package:org.minglg.ai.agent.autoconfig
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/12/25
 * @Version 1.0
 */
@EnableConfigurationProperties(AiAgentProperties.class)
@AutoConfiguration
@ConditionalOnProperty(name = "interview.ai.agent.enable", havingValue = "true")
public class AiAgentAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public AiAgentService aiAgentService(AiAgentProperties aiAgentProperties) {
        return new AiAgentServiceImpl(aiAgentProperties);
    }
}
