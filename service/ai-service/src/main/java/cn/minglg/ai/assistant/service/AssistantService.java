package cn.minglg.ai.assistant.service;

import cn.minglg.ai.advisors.CommonAdvisor;
import cn.minglg.ai.assistant.exception.AssistantCallException;
import cn.minglg.ai.assistant.properties.RoundLimitProperties;
import cn.minglg.commons.model.response.GenericResponse;
import cn.minglg.commons.model.response.ResponseCode;
import cn.minglg.commons.model.task.TaskType;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * ClassName:FluxAssistantService
 * Package:cn.minglg.interview.ai.service
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/13
 * @Version 1.0
 */
@RequiredArgsConstructor
@Service
public class AssistantService {
    private final ChatClient chatClient;
    private final Map<TaskType, PromptTemplate> systemPromptDynamicTemplate;
    private final RoundLimitProperties properties;
    private final List<CommonAdvisor> commonAdvisors;
    private final MessageChatMemoryAdvisor chatMemoryAdvisor;

    /**
     * 执行聊天对话操作
     *
     * @param conversationId 对话ID，用于标识一次完整的对话会话
     * @param userMessage    用户发送的消息内容
     * @param taskType       任务类型，决定使用哪种系统提示模板
     * @param params         聊天参数映射，包含聊天所需的额外参数
     * @return 返回AI模型生成的回复内容
     */
    public GenericResponse<String> chat(String conversationId, String userMessage, TaskType taskType, Map<String, Object> params) {
        // 构建聊天请求对象，包含聊天客户端、提示模板和参数
        PromptTemplate systemPrompt = systemPromptDynamicTemplate.get(taskType);
        ChatRequest chatRequest = new ChatRequest(chatClient, systemPrompt, Objects.requireNonNullElseGet(params, Map::of));
        Map<String, Object> context = Map.of(properties.getConversationIdKey(), conversationId,
                properties.getTaskTypeKey(), taskType);
        ChatClient.ChatClientRequestSpec promptSpec = chatRequest.chatClient.prompt()
                .advisors(a -> a.params(context))
                .advisors(chatMemoryAdvisor);
        // 逐个应用 CommonAdvisor
        for (CommonAdvisor advisor : commonAdvisors) {
            promptSpec = promptSpec.advisors(advisor);
        }
        String renderedSystemPrompt;
        try {
            renderedSystemPrompt = chatRequest.promptTemplate.render(chatRequest.params);
        } catch (Exception e) {
            renderedSystemPrompt = systemPromptDynamicTemplate.get(TaskType.GENERAL_CHAT).render(chatRequest.params);
        }
        try {
            String data = promptSpec
                    .system(renderedSystemPrompt)
                    .user(userMessage)
                    .call()
                    .content();
            return GenericResponse.<String>builder()
                    .code(ResponseCode.OK.getCode())
                    .message("success")
                    .data(data)
                    .build();
        } catch (Exception e) {
            throw new AssistantCallException(this.getClass() + ": " + e.getMessage());
        }


    }

    /**
     * 助手（处理一般文本任务，不带记忆）
     *
     * @param userMessage 用户输入的消息内容
     * @param taskType    任务类型，用于确定系统提示词模板
     * @return 包含AI回复内容的通用响应对象
     */
    public GenericResponse<String> assistant(String userMessage, TaskType taskType) {
        try {
            // 调用聊天客户端生成回复
            String data = chatClient.prompt()
                    .system(systemPromptDynamicTemplate.get(taskType).render())
                    .user(userMessage)
                    .call()
                    .content();

            // 构建成功响应
            return GenericResponse.<String>builder()
                    .code(ResponseCode.OK.getCode())
                    .message("success")
                    .data(data)
                    .build();
        } catch (Exception e) {
            throw new AssistantCallException(this.getClass() + ": " + e.getMessage());
        }
    }

    /**
     * 准备聊天会话，用于模拟面试开始场景
     *
     * @param conversationId 会话ID，用于标识唯一的对话上下文
     * @param params         动态模板参数，用于渲染系统提示词
     * @return GenericResponse<String> 包含聊天响应结果的通用响应对象
     */
    public GenericResponse<String> prepareChat(String conversationId, Map<String, Object> params) {
        // 渲染模拟面试开始的系统提示词模板
        String userMessage = systemPromptDynamicTemplate.get(TaskType.MOCK_INTERVIEW_START).render(params);
        // 调用聊天接口处理模拟面试任务
        return chat(conversationId, userMessage, TaskType.MOCK_INTERVIEW, null);
    }


    /**
     * 内部类封装请求参数
     */
    private record ChatRequest(ChatClient chatClient, PromptTemplate promptTemplate, Map<String, Object> params) {
    }
}
