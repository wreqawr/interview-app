package cn.minglg.interview.ai.core.interview;

import cn.minglg.interview.common.constant.TaskType;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * ClassName:AiInterviewCoreService
 * Package:cn.minglg.interview.ai.core.appointment
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/9
 * @Version 1.0
 */
@RequiredArgsConstructor
@Service
public class AiInterviewCoreService {
    private final ChatClient chatClient;
    private final Map<TaskType, PromptTemplate> systemPromptDynamicTemplate;


    public String interviewOnline(String conversationId, String question, Map<String, Object> variables) {
        return chatClient.prompt()
                .system(systemPromptDynamicTemplate.get(TaskType.MOCK_INTERVIEW).render(variables))
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .user(question)
                .call()
                .content();

    }
}
