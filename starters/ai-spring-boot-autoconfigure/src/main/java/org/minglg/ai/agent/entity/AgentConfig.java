package org.minglg.ai.agent.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClassName:AgentConfig
 * Package:org.minglg.ai.agent.entity
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/12/29
 * @Version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AgentConfig {
    /**
     * 问候语，修改后下次入会生效。默认无。
     */
    @JsonProperty("Greeting")
    private String greeting;
    /**
     * 用户在通话启动前的指令，智能体在通话启动后立即响应这一句话。
     */
    @JsonProperty("WakeUpQuery")
    private String wakeUpQuery;
}
