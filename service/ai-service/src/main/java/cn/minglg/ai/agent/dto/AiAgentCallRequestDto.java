package cn.minglg.ai.agent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * ClassName:AiAgentCallRequestDto
 * Package:cn.minglg.ai.agent.dto
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/12/29
 * @Version 1.0
 */
@Data
public class AiAgentCallRequestDto {
//    @NotBlank(message = "用户id不能为空")
//    @JsonProperty("user_id")
//    private String userId;

    @NotBlank(message = "agentId不能为空")
    @JsonProperty("ai_agent_id")
    private String aiAgentId;

    @JsonProperty("region")
    private String region;
}
