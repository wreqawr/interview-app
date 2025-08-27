package cn.minglg.interview.ai.advisor;

import cn.minglg.ai.advisors.RoundLimitRepository;
import cn.minglg.interview.common.constant.task.TaskType;
import lombok.AllArgsConstructor;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;

import java.util.Map;

/**
 * ClassName:InterviewRoundLimitRepository
 * Package:cn.minglg.interview.ai.advisor
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/27
 * @Version 1.0
 */
@AllArgsConstructor
public class InterviewRoundLimitRepository implements RoundLimitRepository {
    private final PromptTemplate promptTemplate;

    /**
     * 修改当前对话请求
     *
     * @param chatClientRequest 原始请求
     * @param taskTypeString    当前请求任务类型
     * @return 修改后的请求
     */
    @Override
    public ChatClientRequest modifyChatClientRequest(ChatClientRequest chatClientRequest, String taskTypeString) {
        // 第一步：获取当前的对话类型，只有面试才需要限制
        TaskType taskType = TaskType.fromString(taskTypeString);
        if (taskType != TaskType.MOCK_INTERVIEW) {
            return chatClientRequest;
        }
        // 第二步：获得用户输入文本，并构建提示词模板
        String currentUserMessage = chatClientRequest.prompt().getUserMessage().getText();
        Map<String, Object> templateParams = Map.of("currentUserMessage", currentUserMessage);
        String renderedTemplate = promptTemplate.render(templateParams);
        // 第三步：修改请求(只修改prompt，不修改context
        Map<String, Object> context = chatClientRequest.context();
        return ChatClientRequest.builder()
                .prompt(Prompt.builder().content(renderedTemplate).build())
                .context(context)
                .build();
    }
}
