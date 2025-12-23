package org.minglg.ai.advisors;

import reactor.core.publisher.Mono;

/**
 * ClassName:ReactiveRoundLimitManager
 * Package:cn.minglg.ai.advisors
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/28
 * @Version 1.0
 */
public interface ReactiveRoundLimitManager {
    /**
     * 获取当前对话轮数
     *
     * @param conversationId 对话ID
     * @return 当前轮数
     */
    Mono<Integer> getCurrentRound(String conversationId);

    /**
     * 增加对话轮数
     *
     * @param conversationId 对话ID
     * @return Mono<Void> 异步操作结果，无返回值
     */
    Mono<Void> increaseChatRound(String conversationId);

    /**
     * 重置对话轮数
     *
     * @param conversationId 对话ID
     * @return Mono<Void> 异步操作结果，无返回值
     */
    Mono<Void> resetChatRound(String conversationId);
}
