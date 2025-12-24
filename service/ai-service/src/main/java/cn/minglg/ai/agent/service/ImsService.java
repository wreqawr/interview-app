package cn.minglg.ai.agent.service;

import cn.minglg.ai.agent.dto.req.AiAgentInstanceDescribeRequestDto;
import cn.minglg.ai.agent.dto.req.GenerateMessageChatTokenRequestDto;
import cn.minglg.ai.agent.dto.req.RtcAuthTokenRequestDto;
import cn.minglg.ai.agent.dto.res.AiAgentInstanceDescribeResponse;
import cn.minglg.ai.agent.dto.res.GenerateMessageChatTokenResponse;
import cn.minglg.commons.model.response.GenericResponse;


/**
 * IMS服务接口，提供实时通信、消息聊天和AI代理实例相关的功能
 */
public interface ImsService {
    /**
     * 获取RTC认证令牌
     *
     * @param rtcAuthTokenRequestDto RTC认证令牌请求数据传输对象，包含获取认证令牌所需的参数
     * @return GenericResponse<?> 通用响应对象，包含RTC认证令牌相关信息
     */
    GenericResponse<?> getRtcAuthToken(RtcAuthTokenRequestDto rtcAuthTokenRequestDto);

    /**
     * 生成消息聊天令牌
     *
     * @param requestDto 生成消息聊天令牌请求数据传输对象，包含生成聊天令牌所需的参数
     * @return GenericResponse<GenerateMessageChatTokenResponse> 通用响应对象，包含生成的消息聊天令牌响应数据
     */
    GenericResponse<GenerateMessageChatTokenResponse> generateMessageChatToken(GenerateMessageChatTokenRequestDto requestDto);

    /**
     * 描述AI代理实例
     *
     * @param aiAgentDescribeRequestDto AI代理实例描述请求数据传输对象，包含描述AI代理实例所需的参数
     * @return GenericResponse<AiAgentInstanceDescribeResponse> 通用响应对象，包含AI代理实例的描述信息响应数据
     */
    GenericResponse<AiAgentInstanceDescribeResponse> describeAiAgentInstance(AiAgentInstanceDescribeRequestDto aiAgentDescribeRequestDto);
}



