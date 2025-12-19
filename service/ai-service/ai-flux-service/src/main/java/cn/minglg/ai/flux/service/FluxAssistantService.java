package cn.minglg.ai.flux.service;

import cn.minglg.ai.advisors.CommonAdvisor;
import cn.minglg.ai.flux.properties.InterviewRoundLimitProperties;
import cn.minglg.commons.model.task.TaskType;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
public class FluxAssistantService {
    private final ChatClient chatClient;
    private final Map<TaskType, PromptTemplate> systemPromptDynamicTemplate;
    private final InterviewRoundLimitProperties properties;
    private final List<CommonAdvisor> commonAdvisors;
    private final MessageChatMemoryAdvisor chatMemoryAdvisor;

    /**
     * 执行聊天对话操作
     *
     * @param conversationId 会话ID，用于标识一次对话上下文
     * @param userMessage    用户发送的消息内容
     * @param taskType       任务类型，用于确定使用的系统提示模板
     * @param params         聊天参数，用于渲染提示模板和传递额外信息
     * @return Flux<String> 流式返回聊天响应内容
     */
    public Flux<String> chat(String conversationId, String userMessage, TaskType taskType, Map<String, Object> params) {
        return Mono.fromCallable(() -> {
                    // 构建聊天请求对象，包含聊天客户端、提示模板和参数
                    PromptTemplate systemPrompt = systemPromptDynamicTemplate.get(taskType);
                    return new ChatRequest(chatClient, systemPrompt, Objects.requireNonNullElseGet(params, Map::of));
                })
                // 切换到专门的线程池
                .publishOn(properties.getScheduler())
                .flatMapMany(chatRequest -> {
                            Map<String, Object> context = Map.of(properties.getConversationIdKey(), conversationId,
                                    properties.getTaskTypeKey(), taskType);
                            ChatClient.ChatClientRequestSpec promptSpec = chatRequest.chatClient.prompt()
                                    .advisors(a -> a.params(context))
                                    .advisors(chatMemoryAdvisor);

                            // 逐个应用 CommonAdvisor
                            for (CommonAdvisor advisor : commonAdvisors) {
                                promptSpec = promptSpec.advisors(advisor);
                            }
                            String systemPrompt;
                            try {
                                systemPrompt = chatRequest.promptTemplate.render(chatRequest.params);
                            } catch (Exception e) {
                                systemPrompt = systemPromptDynamicTemplate.get(TaskType.GENERAL_CHAT).render(chatRequest.params);
                            }
                            return promptSpec
                                    .system(systemPrompt)
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
