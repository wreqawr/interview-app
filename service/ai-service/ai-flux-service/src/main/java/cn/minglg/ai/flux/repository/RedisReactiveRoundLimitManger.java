package cn.minglg.ai.flux.repository;

import cn.minglg.ai.advisors.ReactiveRoundLimitManager;
import cn.minglg.ai.context.UserContextProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Collections;

/**
 * ClassName:RedisReactiveRoundLimitManger
 * Package:cn.minglg.interview.ai.repository
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/28
 * @Version 1.0
 */
@Slf4j
@RequiredArgsConstructor
@Repository
@ConditionalOnBean({UserContextProvider.class})
public class RedisReactiveRoundLimitManger implements ReactiveRoundLimitManager {

    private final ReactiveRedisTemplate<String, Integer> redisTemplate;
    private final UserContextProvider userContextProvider;
    private final String REDIS_KEY_PREFIX = "interview:round:";
    private final Integer REDIS_EXPIRE_SECONDS = 10 * 60;
    private static final String INCREMENT_AND_EXPIRE_SCRIPT = """
            local current = redis.call('INCR', KEYS[1])
            if current == 1 then
                redis.call('EXPIRE', KEYS[1], ARGV[1])
            end
            return current
            """;


    /**
     * 获取当前对话轮数
     *
     * @param conversationId 对话ID
     * @return 当前轮数
     */
    @Override
    public Mono<Integer> getCurrentRound(String conversationId) {
        return getRedisKeyPrefixMono()
                .flatMap(prefix -> {
                    String redisKey = prefix + conversationId;
                    return redisTemplate.opsForValue().get(redisKey)
                            .defaultIfEmpty(0);
                });
    }

    /**
     * 增加指定对话的聊天轮次计数
     *
     * @param conversationId 对话ID，用于标识唯一的对话记录
     * @return Mono<Void> 异步操作结果，无返回值
     */
    @Override
    public Mono<Void> increaseChatRound(String conversationId) {
        // 获取Redis键前缀，拼接对话ID作为完整的Redis键，然后对键对应的值进行自增操作
        return getRedisKeyPrefixMono()
                .flatMap(prefix -> {
                    String redisKey = prefix + conversationId;
                    RedisScript<Long> redisScript = RedisScript.of(INCREMENT_AND_EXPIRE_SCRIPT, Long.class);
                    return redisTemplate.execute(redisScript,
                                    Collections.singletonList(redisKey),
                                    REDIS_EXPIRE_SECONDS)
                            .cast(Long.class)
                            .doOnError(e ->
                                    log.error("redisKey：{}自增失败！", redisKey, e)
                            ).then();
                })
                .onErrorResume(e -> {
                    log.error("conversationId: {}自增操作失败！", conversationId, e);
                    return Mono.error(e); // 或者返回默认值
                });
    }

    /**
     * 重置聊天回合
     *
     * @param conversationId 会话ID，用于标识特定的聊天会话
     * @return Mono<Void> 异步返回空结果，表示操作完成
     */
    @Override
    public Mono<Void> resetChatRound(String conversationId) {
        // 获取Redis键前缀，并基于会话ID删除对应的Redis键值
        return getRedisKeyPrefixMono()
                .flatMap(prefix -> {
                    String redisKey = prefix + conversationId;
                    return redisTemplate.delete(redisKey);
                })
                .then();
    }


    /**
     * 获取Redis键前缀的Mono对象
     * <p>
     * 该方法构建一个Redis键前缀字符串，格式为：基础前缀 + 用户ID + 分隔符
     * 当无法获取用户ID时，使用默认值0L作为用户ID
     *
     * @return Mono<String> 包含Redis键前缀的响应式对象
     */
    private Mono<String> getRedisKeyPrefixMono() {
        return Mono.fromCallable(() -> {
            Long userId;
            try {
                userId = userContextProvider.getUserId();
            } catch (Exception e) {
                // 获取用户ID失败时使用默认值0L
                userId = 0L;
            }
            // 构建Redis键前缀：基础前缀 + 用户ID + 分隔符
            return REDIS_KEY_PREFIX + userId + ":";
        });
    }

}
