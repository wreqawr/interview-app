package cn.minglg.ai.agent.service;

import cn.minglg.ai.agent.dto.req.AiAgentInstanceDescribeRequestDto;
import cn.minglg.ai.agent.dto.req.GenerateMessageChatTokenRequestDto;
import cn.minglg.ai.agent.dto.req.RtcAuthTokenRequestDto;
import cn.minglg.ai.agent.dto.res.AiAgentInstanceDescribeResponse;
import cn.minglg.ai.agent.dto.res.GenerateMessageChatTokenResponse;
import cn.minglg.ai.agent.dto.res.RtcAuthTokenResponse;


/**
 * IMS服务接口，提供实时通信、消息聊天和AI代理实例相关的认证令牌生成功能。
 */
public interface ImsService {
    /**
     * 获取RTC（实时通信）认证令牌
     *
     * @param rtcAuthTokenRequestDto RTC认证令牌请求参数对象，包含用户ID、房间ID等信息
     * @return RtcAuthTokenResponse RTC认证令牌响应对象，包含生成的认证令牌及相关信息
     */
    RtcAuthTokenResponse getRtcAuthToken(RtcAuthTokenRequestDto rtcAuthTokenRequestDto);

    /**
     * 生成消息聊天认证令牌
     *
     * @param requestDto 消息聊天令牌生成请求参数对象，包含聊天相关配置信息
     * @return GenerateMessageChatTokenResponse 消息聊天令牌生成响应对象，包含生成的聊天认证令牌
     */
    GenerateMessageChatTokenResponse generateMessageChatToken(GenerateMessageChatTokenRequestDto requestDto);

    /**
     * 描述AI代理实例信息
     *
     * @param aiAgentDescribeRequestDto AI代理实例描述请求参数对象，包含实例ID等查询条件
     * @return AiAgentInstanceDescribeResponse AI代理实例描述响应对象，包含实例的详细信息
     */
    AiAgentInstanceDescribeResponse describeAiAgentInstance(AiAgentInstanceDescribeRequestDto aiAgentDescribeRequestDto);
}


