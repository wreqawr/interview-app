package org.minglg.ai.agent.entity;

import cn.minglg.commons.model.response.ResponseCode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AiAgentInstanceDescribeResponse {

    @JsonProperty("code")
    @JsonIgnore
    private ResponseCode code;

    @JsonProperty("error_code")
    private String errorCode;

    @JsonProperty("message")
    private String message;

    @JsonProperty("request_id")
    private String requestId;

    @JsonProperty("call_log_url")
    private String callLogUrl;

    @JsonProperty("runtime_config")
    private String runtimeConfig;

    @JsonProperty("status")
    private String status;

    @JsonProperty("template_config")
    private String templateConfig;

    @JsonProperty("user_data")
    private String userData;
}
