package cn.minglg.ai.agent.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class AiAgentInstanceDescribeRequestDto {

    @NotBlank(message = "用户id不能为空")
    @JsonProperty("user_id")
    private String userId;

    @NotBlank(message = "机器人实例id不能为空")
    @JsonProperty("ai_agent_instance_id")
    private String aiAgentInstanceId;

    @JsonProperty("region")
    private String region;
}
