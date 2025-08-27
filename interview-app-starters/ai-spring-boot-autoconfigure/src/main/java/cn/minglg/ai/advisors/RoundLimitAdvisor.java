package cn.minglg.ai.advisors;

import cn.minglg.ai.properties.RoundLimitProperties;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.ai.chat.client.ChatClientMessageAggregator;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.AdvisorChain;
import org.springframework.ai.chat.client.advisor.api.BaseChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.Map;

/**
 * ClassName:RoundLimitAdvisor
 * Package:cn.minglg.ai.advisors
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/27
 * @Version 1.0
 */
@AllArgsConstructor
public class RoundLimitAdvisor implements BaseChatMemoryAdvisor {
    private final ChatMemory chatMemory;
    private final RoundLimitProperties roundLimitProperties;
    private final RoundLimitRepository roundLimitRepository;
//    protected final int order;
//    protected final String defaultConversationId;
//    protected final Scheduler scheduler;
//    protected final int maxRounds;

    @Override
    public int getOrder() {
        return roundLimitProperties.getOrder();
    }

    @NotNull
    @Override
    public Scheduler getScheduler() {
        return this.roundLimitProperties.getScheduler();
    }

    /**
     * 在聊天请求处理之前执行的拦截方法
     *
     * @param chatClientRequest 聊天客户端请求对象，包含请求上下文和消息内容
     * @param advisorChain      拦截器链，用于继续执行后续的拦截器
     * @return 处理后的聊天客户端请求对象，如果超出轮次限制会返回修改后的请求
     */
    @NotNull
    @Override

    public ChatClientRequest before(@NotNull ChatClientRequest chatClientRequest, @NotNull AdvisorChain advisorChain) {
        // 第一步：获取当前会话ID，以及任务类型
        String conversationId = getConversationId(chatClientRequest.context(), roundLimitProperties.getDefaultConversationId());
        String taskTypeString = getTaskTypeString(chatClientRequest.context(), roundLimitProperties.getDefaultTaskTypeString());
        // 第二步：获取当前的轮次
        int currentRound = Math.toIntExact(this.chatMemory.get(conversationId)
                .stream()
                .filter(message -> message.getMessageType() == MessageType.ASSISTANT)
                .count()) + 1;

        // 判断当前轮次是否超出限制，超出限制则修改请求
        // 适配器模式和模板方法模式，具体的模板方法由用户自行实现
        if (currentRound > roundLimitProperties.getMaxRounds()) {
            return roundLimitRepository.modifyChatClientRequest(chatClientRequest, taskTypeString);
        }
        return chatClientRequest;
    }

    /**
     * 在聊天客户端响应处理链中的后置处理方法
     *
     * @param chatClientResponse 聊天客户端响应对象，不能为空
     * @param advisorChain       处理链对象，用于继续执行后续处理步骤，不能为空
     * @return 经过修改后的聊天客户端响应对象
     */
    @NotNull
    @Override
    public ChatClientResponse after(@NotNull ChatClientResponse chatClientResponse, @NotNull AdvisorChain advisorChain) {
        String taskTypeString = getTaskTypeString(chatClientResponse.context(), roundLimitProperties.getDefaultTaskTypeString());
        return roundLimitRepository.modifyChatClientResponse(chatClientResponse, taskTypeString);
    }

    /**
     * 流式处理聊天客户端请求并返回响应流
     *
     * @param chatClientRequest  聊天客户端请求对象，包含请求信息
     * @param streamAdvisorChain 流式处理链，用于处理请求的各个阶段
     * @return Flux<ChatClientResponse> 返回聊天客户端响应的流
     */
    @NotNull
    @Override
    public Flux<ChatClientResponse> adviseStream(@NotNull ChatClientRequest chatClientRequest,
                                                 @NotNull StreamAdvisorChain streamAdvisorChain) {
        Scheduler scheduler = this.getScheduler();
        return Mono.just(chatClientRequest)
                // 在指定调度器上发布请求并进行前置处理
                .publishOn(scheduler)
                .map(request -> this.before(request, streamAdvisorChain))
                // 执行流式处理链的下一个处理步骤
                .flatMapMany(streamAdvisorChain::nextStream)
                // 聚合聊天客户端响应并进行后置处理
                .transform(flux -> new ChatClientMessageAggregator().aggregateChatClientResponse(flux,
                        response -> this.after(response, streamAdvisorChain)));

    }

    private String getTaskTypeString(Map<String, Object> context, String defaultConversationId) {
        Assert.notNull(context, "context cannot be null");
        Assert.noNullElements(context.keySet().toArray(), "context cannot contain null keys");
        Assert.hasText(defaultConversationId, "defaultConversationId cannot be null or empty");
        return context.containsKey(RoundLimitRepository.TASK_TYPE_STRING) ? context.get(RoundLimitRepository.TASK_TYPE_STRING).toString()
                : roundLimitProperties.getDefaultTaskTypeString();
    }
}
