package cn.minglg.interview.ai.service;

import cn.minglg.ai.advisors.CommonAdvisor;
import cn.minglg.interview.ai.properties.InterviewRoundLimitProperties;
import cn.minglg.interview.constant.TaskType;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
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
    private final ChatClient chatClient;
    private final Map<TaskType, PromptTemplate> systemPromptDynamicTemplate;
    private final InterviewRoundLimitProperties properties;
    private final List<CommonAdvisor> commonAdvisors;
    private final MessageChatMemoryAdvisor chatMemoryAdvisor;

    public Flux<String> chat(String conversationId, String userMessage, TaskType taskType, Map<String, Object> params) {
        return Mono.fromCallable(() -> {
                    // 构建聊天请求对象，包含聊天客户端、提示模板和参数
                    PromptTemplate systemPrompt = systemPromptDynamicTemplate.get(taskType);
                    return new ChatRequest(chatClient, systemPrompt, params);
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
