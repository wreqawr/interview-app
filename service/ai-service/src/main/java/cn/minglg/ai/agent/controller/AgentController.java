package cn.minglg.ai.agent.controller;

import cn.minglg.ai.agent.dto.AiAgentCallRequestDto;
import cn.minglg.ai.agent.dto.AiAgentInstanceDescribeRequestDto;
import cn.minglg.ai.agent.dto.GenerateMessageChatTokenRequestDto;
import cn.minglg.ai.agent.dto.RtcAuthTokenRequestDto;
import cn.minglg.ai.agent.service.ImsService;
import cn.minglg.commons.model.response.GenericResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        GenericResponse<?> response = imsService.generateMessageChatToken(generateMessageChatTokenRequestDto);
        return ResponseEntity.ok(response);
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
        GenericResponse<?> response = imsService.getRtcAuthToken(rtcAuthTokenRequestDto);
        return ResponseEntity.ok(response);
    }


    /**
     * 描述AI代理实例信息接口
     * 该接口用于获取指定AI代理实例的详细信息
     *
     * @param aiAgentDescribeRequestDto AI代理实例描述请求DTO，包含查询AI代理实例所需参数
     * @return ResponseEntity<?> 包含AI代理实例描述响应的通用响应对象
     */
    @PostMapping("/describeAIAgentInstance")
    public ResponseEntity<?> describeAIAgentInstance(@RequestBody @Valid AiAgentInstanceDescribeRequestDto aiAgentDescribeRequestDto) {
        // 调用服务层方法获取AI代理实例信息
        GenericResponse<?> response = imsService.describeAiAgentInstance(aiAgentDescribeRequestDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/generateAIAgentCall")
    public ResponseEntity<?> generateAIAgentCall(@RequestBody @Valid AiAgentCallRequestDto aiAgentCallRequestDto) {
        GenericResponse<?> response = imsService.generateAIAgentCall(aiAgentCallRequestDto);
        return ResponseEntity.ok(response);
    }


}
