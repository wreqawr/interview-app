package cn.minglg.interview.ai.advisor;

import cn.minglg.ai.advisors.CommonAdvisorRepository;
import cn.minglg.ai.advisors.ReactiveRoundLimitManager;
import cn.minglg.interview.ai.properties.InterviewRoundLimitProperties;
import cn.minglg.interview.common.constant.task.TaskType;
import lombok.AllArgsConstructor;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;

import java.util.Map;

/**
 * ClassName:InterviewRoundAdvisorRepository
 * Package:cn.minglg.interview.ai.advisor
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/27
 * @Version 1.0
 */
@AllArgsConstructor
public class InterviewRoundAdvisorRepository implements CommonAdvisorRepository {
    private final PromptTemplate promptTemplate;
    private final InterviewRoundLimitProperties properties;
    private ReactiveRoundLimitManager repository;

    /**
     * 获取advisor顺序
     *
     * @return advisor顺序
     */
    @Override
    public int getOrder() {
        return properties.getOrder();
    }

    /**
     * 修改聊天客户端请求，根据任务类型和对话轮次限制来决定是否需要修改原始请求
     *
     * @param chatClientRequest 原始聊天客户端请求对象，包含上下文和提示信息
     * @return 修改后的聊天客户端请求对象，如果不需要修改则返回原始请求
     */
    @Override
    public ChatClientRequest modifyChatClientRequest(ChatClientRequest chatClientRequest) {
        // 第一步：获取当前会话ID，以及任务类型
        Map<String, Object> context = chatClientRequest.context();
        String conversationId = getContextParam(context, properties.getConversationIdKey(), properties.getDefaultConversationId());
        TaskType taskType = getContextParam(context, properties.getTaskTypeKey(), TaskType.fromString(properties.getDefaultTaskTypeString(), TaskType.GENERAL_CHAT));

        // 第二步：获取当前的轮次
        Integer currentRound = repository.getCurrentRound(conversationId).block();
        currentRound = currentRound == null ? 1 : currentRound + 1;
        // 判断当前对话类型是否需要被拦截，且轮次是否超出限制，超出限制则修改请求
        if (taskType == TaskType.MOCK_INTERVIEW && currentRound > properties.getMaxRounds()) {
            // 第三步：获得用户输入文本，并构建提示词模板
            String currentUserMessage = chatClientRequest.prompt().getUserMessage().getText();
            Map<String, Object> templateParams = Map.of("currentUserMessage", currentUserMessage);
            String renderedTemplate = promptTemplate.render(templateParams);

            // 第三步：修改请求(只修改prompt，不修改context
            return ChatClientRequest.builder()
                    .prompt(Prompt.builder().content(renderedTemplate).build())
                    .context(context)
                    .build();
        }
        return chatClientRequest;
    }


    /**
     * 修改聊天客户端响应对象
     * 默认不做任何操作，后续加入项目有需求可重写此方法
     *
     * @param chatClientResponse 要修改的聊天客户端响应对象
     * @return 修改后的聊天客户端响应对象
     */
    @Override
    public ChatClientResponse modifyChatClientResponse(ChatClientResponse chatClientResponse) {
        Map<String, Object> context = chatClientResponse.context();
        String conversationId = getContextParam(context, properties.getConversationIdKey(), properties.getDefaultConversationId());
        repository.increaseChatRound(conversationId).block();
        return chatClientResponse;
    }
}
