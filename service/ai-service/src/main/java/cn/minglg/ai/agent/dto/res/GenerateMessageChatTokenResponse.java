package cn.minglg.ai.agent.dto.res;

import cn.minglg.commons.model.response.ResponseCode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenerateMessageChatTokenResponse {
    @JsonProperty("code")
    @JsonIgnore
    private ResponseCode code;

    @JsonProperty("message")
    private String message;

    @JsonProperty("error_code")
    private String errorCode;

    @JsonProperty("request_id")
    private String requestId;

    @JsonProperty("app_id")
    private String appId;

    @JsonProperty("token")
    private String token;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("nonce")
    private String nonce;

    @JsonProperty("role")
    private String role;

    @JsonProperty("timestamp")
    private long timestamp;

    @JsonProperty("app_sign")
    private String appSign;
}
