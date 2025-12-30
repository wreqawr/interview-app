package org.minglg.ai.agent.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * ClassName:AgentProperties
 * Package:cn.minglg.ai.agent.properties
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/12/22
 * @Version 1.0
 */
@ConfigurationProperties(prefix = "interview.ai.agent")
@Data
public class AiAgentProperties {
    private boolean enable = false;
    private Access access;
    private LiveMic liveMic;

    @Data
    public static class Access {
        private String keyId;
        private String keySecret;
    }

    @Data
    public static class LiveMic {
        private String appId;
        private String appKey;
        private String region = "cn-shanghai";
        private String voiceAgentId;
    }
}
