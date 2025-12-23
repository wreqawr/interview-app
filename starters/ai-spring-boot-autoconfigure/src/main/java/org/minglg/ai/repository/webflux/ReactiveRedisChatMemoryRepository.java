package org.minglg.ai.repository.webflux;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.jetbrains.annotations.NotNull;
import org.minglg.ai.context.UserContextProvider;
import org.minglg.ai.deserializer.MessageDeserializer;
import org.minglg.ai.properties.AiProperties;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.messages.Message;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

/**
 * ClassName:ReactiveRedisChatMemoryRepository
 * Package:cn.minglg.ai.repository.webflux
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/24
 * @Version 1.0
 */
public class ReactiveRedisChatMemoryRepository implements ChatMemoryRepository {
    private final ReactiveStringRedisTemplate redisTemplate;
    private final String CHAT_MEMORY_REDIS_KEY_PREFIX;
    private final Integer CHAT_MEMORY_REDIS_EXPIRE_DAYS;
    private final UserContextProvider userContextProvider;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ReactiveRedisChatMemoryRepository(
            ReactiveStringRedisTemplate redisTemplate,
            AiProperties aiProperties,
            UserContextProvider userContextProvider) {
        this.redisTemplate = redisTemplate;
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Message.class, new MessageDeserializer());
        this.objectMapper.registerModule(module);
        this.CHAT_MEMORY_REDIS_KEY_PREFIX = aiProperties.getChatMemoryRedisKeyPrefix() + ":";
        this.CHAT_MEMORY_REDIS_EXPIRE_DAYS = aiProperties.getChatMemoryRedisExpireDays();
        this.userContextProvider = userContextProvider;
    }


    /**
     * 查找所有会话ID列表
     *
     * @return 会话ID字符串列表，不允许为null
     */
    @NotNull
    @Override
    public List<String> findConversationIds() {
        // 完全响应式，不使用.block()
        return Objects.requireNonNull(getRedisKeyPrefixMono()
                .flatMap(prefix -> redisTemplate.keys(prefix + "*")
                        .map(key -> key.substring(prefix.length()))
                        .collectList())
                .block()); // 只在最后转换为阻塞式结果

    }


    /**
     * 根据会话ID查找消息列表
     *
     * @param conversationId 会话ID，用于构建Redis键值，不能为空
     * @return 返回指定会话ID对应的所有消息列表，消息按存储顺序排列
     */
    @NotNull
    @Override
    public List<Message> findByConversationId(@NotNull String conversationId) {
        return Objects.requireNonNull(getRedisKeyPrefixMono()
                .flatMap(prefix -> {
                    String redisKey = prefix + conversationId;
                    return redisTemplate.opsForList()
                            .range(redisKey, 0, -1)
                            .flatMap(messageString -> {
                                try {
                                    Message message = objectMapper.readValue(messageString, Message.class);
                                    return Mono.just(message);
                                } catch (JsonProcessingException e) {
                                    return Mono.error(new RuntimeException("反序列化失败！", e));
                                }
                            })
                            .collectList();
                })
                .block());
    }

    /**
     * 保存对话的所有消息到Redis中
     *
     * @param conversationId 对话ID，用于标识特定的对话，不能为空
     * @param messages       消息列表，包含要保存的所有消息，不能为空
     */
    @Override
    public void saveAll(@NotNull String conversationId, @NotNull List<Message> messages) {
        getRedisKeyPrefixMono()
                .flatMap(prefix -> {
                    String redisKey = prefix + conversationId;
                    return redisTemplate.delete(redisKey)
                            .then(
                                    Flux.fromIterable(messages)
                                            .flatMap(message -> {
                                                try {
                                                    String messageString = objectMapper.writeValueAsString(message);
                                                    return redisTemplate.opsForList().rightPush(redisKey, messageString);
                                                } catch (JsonProcessingException e) {
                                                    return Mono.error(new RuntimeException("将Message序列成String失败！", e));
                                                }
                                            })
                                            .then(Mono.defer(
                                                    () -> redisTemplate.expire(redisKey, Duration.ofDays(CHAT_MEMORY_REDIS_EXPIRE_DAYS))
                                                            .then()
                                            )));
                })
                .block();
    }


    /**
     * 根据会话ID删除Redis中的数据
     *
     * @param conversationId 会话ID，不能为空
     */
    @Override
    public void deleteByConversationId(@NotNull String conversationId) {
        getRedisKeyPrefixMono()
                .flatMap(prefix -> {
                    String redisKey = prefix + conversationId;
                    return redisTemplate.delete(redisKey);
                })
                .block();
    }


    /**
     * 获取Redis键前缀的Mono对象
     * 该方法通过用户上下文提供者获取当前用户ID，并构建Redis键前缀。
     * 如果获取用户ID过程中发生异常，则使用默认用户ID 0L。
     * 最终返回的Redis键前缀格式为：CHAT_MEMORY_REDIS_KEY_PREFIX + userId + ":"
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
            return CHAT_MEMORY_REDIS_KEY_PREFIX + userId + ":";
        });
    }


}
