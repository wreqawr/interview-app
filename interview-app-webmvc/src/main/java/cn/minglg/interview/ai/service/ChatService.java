package cn.minglg.interview.ai.service;

import cn.minglg.ai.constant.ChatClientType;
import cn.minglg.interview.common.constant.task.TaskType;
import cn.minglg.interview.common.response.R;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * ClassName:ChatService
 * Package:cn.minglg.interview.ai.service
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/13
 * @Version 1.0
 */
@RequiredArgsConstructor
@Service
public class ChatService {
    private final Map<ChatClientType, ChatClient> chatClientMap;
    private final Map<TaskType, PromptTemplate> systemPromptDynamicTemplate;
    private final ToolService toolService;

    /**
     * 执行通用聊天对话
     *
     * @param conversationId 对话ID，用于标识和管理对话历史
     * @param userMessage    用户发送的聊天消息内容
     * @return 聊天机器人返回的响应内容
     */
    public R generalChat(String conversationId, String userMessage) {
        // 获取通用带记忆功能的聊天客户端
        ChatClient chatClient = chatClientMap.get(ChatClientType.GENERAL_WITH_MEMORY);

        // 构建并执行聊天请求
        String data = chatClient.prompt()
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .tools(toolService)
                .system(systemPromptDynamicTemplate.get(TaskType.GENERAL_CHAT).render())
                .user(userMessage)
                .call()
                .content();
        return R.builder()
                .code(200)
                .data(data)
                .message("请求成功！")
                .build();
    }

}
