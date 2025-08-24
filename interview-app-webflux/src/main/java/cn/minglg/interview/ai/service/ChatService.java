package cn.minglg.interview.ai.service;

import cn.minglg.ai.constant.ChatClientType;
import cn.minglg.interview.common.constant.task.TaskType;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

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
    private final Scheduler blockingScheduler = Schedulers.boundedElastic();

    public Flux<String> chat(String conversationId, String userMessage, TaskType taskType, Map<String, Object> params) {
        return Mono.fromCallable(() -> {
                    // 将阻塞操作包装在Mono.fromCallable中
                    ChatClient chatClient = chatClientMap.get(ChatClientType.GENERAL_WITH_MEMORY);
                    PromptTemplate systemPrompt = systemPromptDynamicTemplate.get(taskType);
                    PromptTemplate defaultPrompt = systemPromptDynamicTemplate.get(TaskType.GENERAL_CHAT);
                    PromptTemplate promptTemplate = systemPrompt != null ? systemPrompt : defaultPrompt;
                    return new ChatRequest(chatClient, promptTemplate, params);
                })
                // 切换到专门的线程池
                .publishOn(blockingScheduler)
                .flatMapMany(chatRequest ->
                        chatRequest.chatClient.prompt()
                                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                                .system(chatRequest.promptTemplate.render(chatRequest.params))
                                .user(userMessage)
                                .stream()
                                .content()
                );
    }

    /**
     * 内部类封装请求参数
     */
    private record ChatRequest(ChatClient chatClient, PromptTemplate promptTemplate, Map<String, Object> params) {
    }
}
