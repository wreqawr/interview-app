package cn.minglg.ai.advisors;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.ai.chat.client.ChatClientMessageAggregator;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.AdvisorChain;
import org.springframework.ai.chat.client.advisor.api.BaseAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

/**
 * ClassName:CommonAdvisor
 * Package:cn.minglg.ai.advisors
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/27
 * @Version 1.0
 */
@AllArgsConstructor
public class CommonAdvisor implements BaseAdvisor {
    private final CommonAdvisorRepository commonAdvisorRepository;

    @Override
    public int getOrder() {
        return commonAdvisorRepository.getOrder();
    }

    @NotNull
    @Override
    public Scheduler getScheduler() {
        return this.commonAdvisorRepository.getScheduler();
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
        return commonAdvisorRepository.modifyChatClientRequest(chatClientRequest);
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
        return commonAdvisorRepository.modifyChatClientResponse(chatClientResponse);
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

}
