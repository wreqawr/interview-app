package org.minglg.ai.agent.service;

import org.minglg.ai.agent.entity.AgentConfig;
import org.minglg.ai.agent.entity.AiAgentInstanceDescribeResponse;
import org.minglg.ai.agent.entity.GenerateAIAgentCallResponse;
import org.minglg.ai.agent.entity.GenerateMessageChatTokenResponse;

/**
 * AI代理服务接口
 * 提供AI代理相关的消息生成和实例描述功能
 */
public interface AiAgentService {

    /**
     * 生成消息聊天令牌
     * 用于获取与AI代理进行聊天所需的认证令牌
     *
     * @param aiAgentId AI代理ID，标识特定的AI代理实例
     * @param role      用户角色，定义用户在对话中的身份
     * @param userId    用户ID，标识发起请求的用户
     * @param expire    令牌过期时间（秒），指定令牌的有效期限
     * @param region    区域信息，指定服务部署的地理区域
     * @return GenerateMessageChatTokenResponse 生成的消息聊天令牌响应对象
     */
    GenerateMessageChatTokenResponse generateMessageChatToken(String aiAgentId, String role, String userId, Integer expire, String region);

    /**
     * 描述AI代理实例
     * 获取指定AI代理实例的详细信息
     *
     * @param aiAgentInstanceId AI代理实例ID，标识特定的AI代理实例
     * @param region            区域信息，指定服务部署的地理区域
     * @return AiAgentInstanceDescribeResponse AI代理实例描述响应对象
     */
    AiAgentInstanceDescribeResponse describeAiAgentInstance(String aiAgentInstanceId, String region);

    /**
     * 生成AI智能体调用响应
     * 该方法用于根据指定的AI智能体ID、区域和智能体配置来生成AI智能体的调用响应
     *
     * @param aiAgentId   AI智能体的唯一标识符，用于识别特定的AI智能体实例
     * @param region      服务区域信息，指定AI智能体服务的地理区域或环境
     * @param agentConfig AI智能体的配置参数对象，包含智能体的运行配置和参数设置
     * @return GenerateAIAgentCallResponse 返回AI智能体调用的响应结果，包含调用状态和相关数据
     */
    GenerateAIAgentCallResponse generateAIAgentCall(String aiAgentId, String region, AgentConfig agentConfig);
}


