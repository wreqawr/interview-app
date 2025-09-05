package cn.minglg.interview.ai.advisor;

import cn.minglg.ai.advisors.CommonAdvisorRepository;
import cn.minglg.ai.advisors.ReactiveRoundLimitManager;
import cn.minglg.commons.constant.task.TaskType;
import cn.minglg.interview.ai.properties.InterviewRoundLimitProperties;
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
        TaskType taskType = getContextParam(context, properties.getTaskTypeKey(), properties.getDefaultTaskType());

        // 第二步：获取当前的轮次
        Integer currentRound = repository.getCurrentRound(conversationId).block();
        currentRound = currentRound == null ? 1 : currentRound + 1;
        // 判断当前对话类型是否是面试
        if (taskType == TaskType.MOCK_INTERVIEW) {
            // 当前请求是否超出最大会话轮数限制
            // 第三步：获得用户输入文本，并构建提示词模板
            String currentUserMessage = chatClientRequest.prompt().getUserMessage().getText();
            if (currentRound > properties.getMaxRounds()) {
                Map<String, Object> templateParams = Map.of("currentUserMessage", currentUserMessage);
                String renderedTemplate = promptTemplate.render(templateParams);
                // 第四步：修改请求(只修改prompt，不修改context)
                return ChatClientRequest.builder()
                        .prompt(Prompt.builder().content(renderedTemplate).build())
                        .context(context)
                        .build();
            }
            // 如果没有超过最大轮数限制，则要求ai对上一个回答进行点评
            // 非首轮才需要点评
            if (currentRound > 1) {
                String renderedAnswer = "以下是我的回答：\n" + currentUserMessage + "\n请先对以上回答进行点评，并给出建议。然后接着问下一个问题";
                return ChatClientRequest.builder()
                        .prompt(Prompt.builder().content(renderedAnswer).build())
                        .context(context)
                        .build();
            }
            return chatClientRequest;
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
