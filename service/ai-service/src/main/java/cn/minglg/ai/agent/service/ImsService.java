package cn.minglg.ai.agent.service;

import cn.minglg.ai.agent.dto.AiAgentInstanceDescribeRequestDto;
import cn.minglg.ai.agent.dto.GenerateMessageChatTokenRequestDto;
import cn.minglg.ai.agent.dto.RtcAuthTokenRequestDto;
import cn.minglg.commons.model.response.GenericResponse;


/**
 * IMS服务接口，提供实时通信、消息聊天和AI代理实例相关的功能
 */
public interface ImsService {
    /**
     * 获取RTC认证令牌
     *
     * @param rtcAuthTokenRequestDto RTC认证令牌请求数据传输对象，包含获取令牌所需的参数信息
     * @return GenericResponse<?> 通用响应对象，包含RTC认证令牌相关信息
     */
    GenericResponse<?> getRtcAuthToken(RtcAuthTokenRequestDto rtcAuthTokenRequestDto);

    /**
     * 生成消息聊天令牌
     *
     * @param requestDto 生成消息聊天令牌请求数据传输对象，包含生成聊天令牌所需的参数信息
     * @return GenericResponse<?> 通用响应对象，包含消息聊天令牌相关信息
     */
    GenericResponse<?> generateMessageChatToken(GenerateMessageChatTokenRequestDto requestDto);

    /**
     * 查询AI代理实例信息
     *
     * @param aiAgentDescribeRequestDto AI代理实例查询请求数据传输对象，包含查询AI代理实例所需的参数信息
     * @return GenericResponse<?> 通用响应对象，包含AI代理实例的详细信息
     */
    GenericResponse<?> describeAiAgentInstance(AiAgentInstanceDescribeRequestDto aiAgentDescribeRequestDto);
}




