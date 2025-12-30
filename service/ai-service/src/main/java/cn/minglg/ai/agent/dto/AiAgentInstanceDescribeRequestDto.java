package cn.minglg.ai.agent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class AiAgentInstanceDescribeRequestDto {

    @NotBlank(message = "agent实例id不能为空")
    @JsonProperty("ai_agent_instance_id")
    private String aiAgentInstanceId;

    @JsonProperty("region")
    private String region;
}
