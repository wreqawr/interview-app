package cn.minglg.ai.assistant.repository;

import cn.minglg.ai.advisors.RoundLimitManager;
import cn.minglg.ai.context.UserContextProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Repository;

import java.util.Collections;

/**
 * ClassName:RedisRoundLimitManger
 * Package:cn.minglg.interview.ai.repository
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/28
 * @Version 1.0
 */
@RequiredArgsConstructor
@Repository
@ConditionalOnBean({UserContextProvider.class})
public class RedisRoundLimitManger implements RoundLimitManager {

    private final RedisTemplate<String, Integer> redisTemplate;
    private final UserContextProvider userContextProvider;
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
    public Integer getCurrentRound(String conversationId) {
        String prefix = getRedisKeyPrefix();
        String redisKey = prefix + conversationId;
        Integer currentRound = redisTemplate.opsForValue().get(redisKey);
        return currentRound == null ? 0 : currentRound;
    }


    /**
     * 增加聊天轮次计数
     * 该方法通过Redis脚本对指定对话的聊天轮次进行原子性自增操作，并设置过期时间
     *
     * @param conversationId 对话ID，用于标识唯一的对话记录
     */
    @Override
    public void increaseChatRound(String conversationId) {
        // 获取Redis键前缀，拼接对话ID作为完整的Redis键，然后对键对应的值进行自增操作
        String prefix = getRedisKeyPrefix();
        String redisKey = prefix + conversationId;
        RedisScript<Long> redisScript = RedisScript.of(INCREMENT_AND_EXPIRE_SCRIPT, Long.class);
        Integer REDIS_EXPIRE_SECONDS = 10 * 60;
        redisTemplate.execute(redisScript,
                Collections.singletonList(redisKey),
                REDIS_EXPIRE_SECONDS);
    }


    /**
     * 重置聊天回合
     *
     * @param conversationId 会话ID，用于标识特定的对话
     */
    @Override
    public void resetChatRound(String conversationId) {
        // 获取Redis键前缀，并基于会话ID删除对应的Redis键值
        String prefix = getRedisKeyPrefix();
        String redisKey = prefix + conversationId;
        redisTemplate.delete(redisKey);
    }


    /**
     * 获取Redis键前缀
     *
     * @return Redis键前缀字符串，格式为：基础前缀 + 用户ID + ":"
     */
    private String getRedisKeyPrefix() {
        Long userId;
        try {
            userId = userContextProvider.getUserId();
        } catch (Exception e) {
            // 获取用户ID失败时使用默认值0L
            userId = 0L;
        }
        // 构建Redis键前缀：基础前缀 + 用户ID + 分隔符
        String REDIS_KEY_PREFIX = "interview:round:";
        return REDIS_KEY_PREFIX + userId + ":";
    }

}
