package cn.minglg.interview.ai.repository;

import cn.minglg.interview.auth.pojo.User;
import cn.minglg.interview.common.properties.GlobalProperties;
import cn.minglg.interview.common.utils.UserUtils;
import com.alibaba.cloud.ai.memory.redis.serializer.MessageDeserializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.jetbrains.annotations.NotNull;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.messages.Message;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * ClassName:CustomRedisChatMemoryRepository
 * Package:cn.minglg.interview.ai.repository
 * Description:自定义的redis上下文持久化
 *
 * @Author kfzx-minglg
 * @Create 2025/8/10
 * @Version 1.0
 */
@Repository("redisChatMemoryRepository")
public class CustomRedisChatMemoryRepository implements ChatMemoryRepository {
    private final StringRedisTemplate redisTemplate;
    /**
     * 持久化上下文记忆的redis前缀
     */
    private final String CHAT_MEMORY_REDIS_KEY_PREFIX;
    private final Long CHAT_MEMORY_REDIS_EXPIRE_DAYS;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CustomRedisChatMemoryRepository(StringRedisTemplate redisTemplate,
                                           GlobalProperties globalProperties) {
        this.redisTemplate = redisTemplate;
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Message.class, new MessageDeserializer());
        this.objectMapper.registerModule(module);
        this.CHAT_MEMORY_REDIS_KEY_PREFIX = globalProperties.getAi().getChatMemoryRedisKeyPrefix() + ":";
        this.CHAT_MEMORY_REDIS_EXPIRE_DAYS = globalProperties.getAi().getChatMemoryRedisExpireDays();
    }

    /**
     * 返回所有的conversationId
     *
     * @return 返回所有的conversationId
     */
    @NotNull
    @Override
    public List<String> findConversationIds() {
        String redisKeyPrefix = getRedisKeyPrefix();
        Set<String> keySet = redisTemplate.keys(redisKeyPrefix + "*");
        return keySet.stream().map(key -> key.substring(redisKeyPrefix.length())).toList();
    }

    /**
     * 根据conversationId查找所有历史记录
     *
     * @param conversationId 对话id
     * @return 历史记录
     */
    @NotNull
    @Override
    public List<Message> findByConversationId(@NotNull String conversationId) {
        String redisKey = getRedisKeyPrefix() + conversationId;
        List<String> messageStringList = redisTemplate.opsForList().range(redisKey, 0, -1);
        List<Message> messages = new ArrayList<>();
        if (messageStringList != null) {
            for (String messageString : messageStringList) {
                try {
                    Message message = objectMapper.readValue(messageString, Message.class);
                    messages.add(message);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException("将redis中的消息反序列成Message对象失败！", e);
                }
            }
        }
        return messages;
    }

    /**
     * Replaces all the existing messages for the given conversation ID with the provided
     * messages.
     *
     * @param conversationId 会话id
     * @param messages       消息列表
     */
    @Override
    public void saveAll(@NotNull String conversationId, @NotNull List<Message> messages) {
        deleteByConversationId(conversationId);
        String redisKey = getRedisKeyPrefix() + conversationId;
        for (Message message : messages) {
            try {
                String messageString = objectMapper.writeValueAsString(message);
                redisTemplate.opsForList().rightPush(redisKey, messageString);
                redisTemplate.expire(redisKey, CHAT_MEMORY_REDIS_EXPIRE_DAYS, TimeUnit.DAYS);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("将Message序列成String失败！", e);
            }
        }
    }

    @Override
    public void deleteByConversationId(@NotNull String conversationId) {
        String redisKey = getRedisKeyPrefix() + conversationId;
        redisTemplate.delete(redisKey);
    }

    /**
     * 清除超过最大限制的历史会话，保留最新会话记录
     *
     * @param conversationId 会话id
     * @param maxLimit       最大的条数限制
     * @param deleteSize     要删除的条数
     */
    public void clearOverLimit(@NotNull String conversationId, int maxLimit, int deleteSize) {
        String redisKey = getRedisKeyPrefix() + conversationId;
        List<String> allMessage = redisTemplate.opsForList().range(redisKey, 0, -1);
        if (allMessage != null && allMessage.size() >= maxLimit) {
            allMessage = allMessage.stream().skip(Math.max(0, deleteSize)).toList();
            deleteByConversationId(conversationId);
            for (String message : allMessage) {
                redisTemplate.opsForList().rightPush(redisKey, message);
            }
            redisTemplate.expire(redisKey, CHAT_MEMORY_REDIS_EXPIRE_DAYS, TimeUnit.DAYS);
        }
    }

    /**
     * 获取当前用户所有对话的redisKey前缀
     *
     * @return redisKey前缀
     */
    private String getRedisKeyPrefix() {
        Long userId;
        try {
            User currentUser = UserUtils.getCurrentUser();
            userId = currentUser.getUserId();
        } catch (Exception e) {
            userId = 0L;
        }
        System.out.println("------------------");
        System.out.println(userId);
        return CHAT_MEMORY_REDIS_KEY_PREFIX + userId + ":";
    }
}
