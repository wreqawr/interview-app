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


    public String interviewOnline(String conversationId, String question) {
        return chatClient.prompt()
                .system("你是一位资深技术面试官，负责基于候选人的简历进行结构化一问一答式技术面试。")
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .user(question)
                .call()
                .content();

    }
}
