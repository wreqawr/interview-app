package org.minglg.ai.agent.entity;

import cn.minglg.commons.model.response.ResponseCode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClassName:GenerateAIAgentCallResponse
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
public class GenerateAIAgentCallResponse {
    @JsonIgnore
    private ResponseCode code;
    @JsonProperty("request_id")
    private String requestId;
    @JsonProperty("ai_agent_instance_id")
    private String instanceId;
    @JsonProperty("ai_agent_user_id")
    private String userId;
    @JsonProperty("workflow_type")
    private String workflowType;
    @JsonProperty("rtc_auth_token")
    private String token;
    @JsonProperty("channel_id")
    private String channelId;
    @JsonProperty("ai_agent_user_id")
    @JsonIgnore
    private String aiAgentUserId;
    private String message;
}
