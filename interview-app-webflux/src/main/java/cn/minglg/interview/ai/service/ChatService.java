package cn.minglg.interview.ai.service;

import cn.minglg.ai.advisors.CommonAdvisor;
import cn.minglg.ai.constant.ChatClientType;
import cn.minglg.interview.ai.properties.InterviewRoundLimitProperties;
import cn.minglg.interview.common.constant.task.TaskType;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
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
    private final InterviewRoundLimitProperties properties;
    private final List<CommonAdvisor> commonAdvisors;

    /**
     * 执行聊天对话操作，支持不同的任务类型和系统提示模板
     *
     * @param conversationId 对话ID，用于标识和管理对话历史
     * @param userMessage    用户发送的聊天消息内容
     * @param taskType       任务类型，决定使用哪种系统提示模板
     * @param params         额外的参数映射，用于渲染提示模板
     * @return Flux<String> 流式返回聊天响应内容
     */
    public Flux<String> chat(String conversationId, String userMessage, TaskType taskType, Map<String, Object> params) {
        return Mono.fromCallable(() -> {
                    // 构建聊天请求对象，包含聊天客户端、提示模板和参数
                    ChatClient chatClient = chatClientMap.get(ChatClientType.GENERAL_WITH_MEMORY);
                    PromptTemplate systemPrompt = systemPromptDynamicTemplate.get(taskType);
                    return new ChatRequest(chatClient, systemPrompt, params);
                })
                // 切换到专门的线程池
                .publishOn(properties.getScheduler())
                .flatMapMany(chatRequest -> {
                            Map<String, Object> context = Map.of(properties.getConversationIdKey(), conversationId,
                                    properties.getTaskTypeKey(), taskType);
                            ChatClient.ChatClientRequestSpec promptSpec = chatRequest.chatClient.prompt()
                                    .advisors(a -> a.params(context));

                            // 逐个应用 CommonAdvisor
                            for (CommonAdvisor advisor : commonAdvisors) {
                                promptSpec = promptSpec.advisors(advisor);
                            }

                            return promptSpec
                                    .system(chatRequest.promptTemplate.render(chatRequest.params))
                                    .user(userMessage)
                                    .stream()
                                    .content();
                        }
                );
    }


    /**
     * 内部类封装请求参数
     */
    private record ChatRequest(ChatClient chatClient, PromptTemplate promptTemplate, Map<String, Object> params) {
    }
}
