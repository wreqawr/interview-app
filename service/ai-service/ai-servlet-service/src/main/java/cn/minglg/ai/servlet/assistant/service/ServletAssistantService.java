package cn.minglg.ai.servlet.assistant.service;

import cn.minglg.ai.servlet.assistant.exception.AssistantCallException;
import cn.minglg.commons.model.response.GenericResponse;
import cn.minglg.commons.model.response.ResponseCode;
import cn.minglg.commons.model.task.TaskType;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * ClassName:ServletAssistantService
 * Package:cn.minglg.ai.servlet.assistant.resume.service
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/12/19
 * @Version 1.0
 */
@RequiredArgsConstructor
@Service
public class ServletAssistantService {
    private final ChatClient chatClient;
    private final Map<TaskType, PromptTemplate> systemPromptTemplate;

    public GenericResponse<String> chat(String userMessage, TaskType taskType) {
        try {
            String data = chatClient.prompt()
                    .system(systemPromptTemplate.get(taskType).render())
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
}
