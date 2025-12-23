package cn.minglg.ai.agent.controller;

import cn.minglg.ai.agent.dto.req.AiAgentInstanceDescribeRequestDto;
import cn.minglg.ai.agent.dto.req.GenerateMessageChatTokenRequestDto;
import cn.minglg.ai.agent.dto.req.RtcAuthTokenRequestDto;
import cn.minglg.ai.agent.dto.res.AiAgentInstanceDescribeResponse;
import cn.minglg.ai.agent.dto.res.GenerateMessageChatTokenResponse;
import cn.minglg.ai.agent.dto.res.RtcAuthTokenResponse;
import cn.minglg.ai.agent.service.ImsService;
import cn.minglg.commons.model.response.GenericResponse;
import cn.minglg.commons.model.response.ResponseCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName:AgentController
 * Package:cn.minglg.ai.agent.controller
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/12/22
 * @Version 1.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/ai/agent")
@Validated
public class AgentController {
    private final ImsService imsService;

    /**
     * 生成消息聊天Token接口
     * 该接口用于生成会话聊天所需的Token及相关信息
     *
     * @param generateMessageChatTokenRequestDto 生成消息聊天Token请求参数对象，包含必要的请求信息
     * @return ResponseEntity<?> 包含生成结果的响应实体，成功时返回Token相关信息，失败时返回错误信息
     */
    @PostMapping("generateMessageChatToken")
    public ResponseEntity<?> generateMessageChatToken(@RequestBody @Valid GenerateMessageChatTokenRequestDto generateMessageChatTokenRequestDto) {
        GenerateMessageChatTokenResponse response = imsService.generateMessageChatToken(generateMessageChatTokenRequestDto);

        // 检查服务返回结果是否为空
        if (response == null) {
            GenericResponse<GenerateMessageChatTokenResponse> result = GenericResponse.<GenerateMessageChatTokenResponse>builder()
                    .code(ResponseCode.GENERATE_MESSAGE_CHAT_TOKEN_ERROR.getCode())
                    .message("生成会话Token失败！")
                    .build();
            return ResponseEntity.ok(result);
        }

        // 处理服务调用成功的情况
        if (200 == response.getCode()) {
            Map<String, Object> map = new HashMap<>(1);
            map.put("app_id", response.getAppId());
            map.put("token", response.getToken());
            map.put("user_id", response.getUserId());
            map.put("nonce", response.getNonce());
            map.put("role", response.getRole());
            map.put("timestamp", response.getTimestamp());
            map.put("app_sign", response.getAppSign());
            map.put("request_id", response.getRequestId());
            map.put("message", response.getMessage());
            GenericResponse<?> result = GenericResponse.builder()
                    .code(ResponseCode.OK.getCode())
                    .data(map)
                    .message("生成会话Token成功！")
                    .build();
            return ResponseEntity.ok(result);
        } else {
            // 处理服务调用失败的情况
            GenericResponse<?> result = GenericResponse.builder()
                    .code(ResponseCode.GENERATE_MESSAGE_CHAT_TOKEN_ERROR.getCode())
                    .data(response)
                    .message("生成会话Token失败！")
                    .build();
            return ResponseEntity.ok(result);
        }
    }


    /**
     * 获取RTC认证令牌接口
     * 该接口用于生成RTC实时通信的认证令牌，包含认证令牌、时间戳和频道ID信息
     *
     * @param rtcAuthTokenRequestDto RTC认证令牌请求DTO，包含生成令牌所需的参数
     * @return ResponseEntity<?> 包含RTC认证令牌信息的响应实体
     * 返回数据包含：rtc_auth_token(认证令牌)、timestamp(时间戳)、channel_id(频道ID)
     */
    @PostMapping("/getRtcAuthToken")
    public ResponseEntity<?> getRtcAuthToken(@RequestBody @Valid RtcAuthTokenRequestDto rtcAuthTokenRequestDto) {
        // 调用服务层获取RTC认证令牌
        RtcAuthTokenResponse rtcAuthTokenResponse = imsService.getRtcAuthToken(rtcAuthTokenRequestDto);

        // 构建返回数据映射
        Map<String, Object> map = new HashMap<>(1);
        map.put("rtc_auth_token", rtcAuthTokenResponse.getAuthToken());
        map.put("timestamp", rtcAuthTokenResponse.getTimestamp());
        map.put("channel_id", rtcAuthTokenRequestDto.getChannelId());

        // 构建通用响应结果
        GenericResponse<Map<String, Object>> result = GenericResponse.<Map<String, Object>>builder()
                .code(ResponseCode.OK.getCode())
                .data(map)
                .message("获取RTC认证令牌成功！")
                .build();

        return ResponseEntity.ok(result);
    }


    /**
     * 获取AI代理实例详细信息的接口
     * 该接口接收AI代理实例描述请求参数，调用服务层获取实例信息，并根据响应结果返回相应的格式化数据
     *
     * @param aiAgentDescribeRequestDto AI代理实例描述请求DTO，包含查询AI代理实例所需的基本信息
     * @return ResponseEntity<?> 包含AI代理实例详细信息的响应实体，包括状态码、消息和数据
     */
    @PostMapping("/describeAIAgentInstance")
    public ResponseEntity<?> describeAIAgentInstance(@RequestBody @Valid AiAgentInstanceDescribeRequestDto aiAgentDescribeRequestDto) {
        // 调用服务层方法获取AI代理实例信息
        AiAgentInstanceDescribeResponse response = imsService.describeAiAgentInstance(aiAgentDescribeRequestDto);
        if (response == null) {
            // 构建服务调用失败的响应结果
            GenericResponse<?> result = GenericResponse.builder()
                    .code(ResponseCode.DESCRIBE_AI_AGENT_INSTANCE_ERROR.getCode())
                    .message("获取AI代理实例信息失败！")
                    .build();
            return ResponseEntity.ok(result);
        }
        if (200 == response.getCode()) {
            // 构建成功响应的数据映射，包含AI代理实例的各项配置信息
            Map<String, Object> map = new HashMap<>(1);
            map.put("code", response.getCode());
            map.put("message", response.getMessage());
            map.put("request_id", response.getRequestId());
            map.put("call_log_url", response.getCallLogUrl());
            map.put("runtime_config", response.getRuntimeConfig());
            map.put("status", response.getStatus());
            map.put("template_config", response.getTemplateConfig());
            map.put("user_data", response.getUserData());
            GenericResponse<?> result = GenericResponse.builder()
                    .code(ResponseCode.OK.getCode())
                    .message("获取AI代理实例信息成功！")
                    .data(map)
                    .build();
            return ResponseEntity.ok(result);
        } else {
            // 构建业务处理失败的响应结果，直接返回原始响应数据
            GenericResponse<?> result = GenericResponse.builder()
                    .code(ResponseCode.DESCRIBE_AI_AGENT_INSTANCE_ERROR.getCode())
                    .data(response)
                    .message("获取AI代理实例信息失败！")
                    .build();
            return ResponseEntity.ok(result);
        }
    }

}
