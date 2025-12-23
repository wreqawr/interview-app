package org.minglg.ai.repository.webflux;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.jetbrains.annotations.NotNull;
import org.minglg.ai.context.UserContextProvider;
import org.minglg.ai.deserializer.MessageDeserializer;
import org.minglg.ai.pojo.ChatHistory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.messages.Message;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * ClassName:ReactiveMongoChatMemoryRepository
 * Package:cn.minglg.ai.repository.webflux
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/25
 * @Version 1.0
 */
public class ReactiveMongoChatMemoryRepository implements ChatMemoryRepository {
    private final ReactiveMongoTemplate mongoTemplate;
    private final UserContextProvider userContextProvider;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ReactiveMongoChatMemoryRepository(ReactiveMongoTemplate mongoTemplate,
                                             UserContextProvider userContextProvider) {
        this.mongoTemplate = mongoTemplate;
        this.userContextProvider = userContextProvider;
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Message.class, new MessageDeserializer());
        this.objectMapper.registerModule(module);
    }

    @NotNull
    @Override
    public List<String> findConversationIds() {
        return Objects.requireNonNull(getUserIdMono()
                .flatMap(userId -> {
                    Query query = new Query(
                            Criteria.where("userId").is(userId)
                    );
                    return mongoTemplate.find(query, ChatHistory.class)
                            .map(ChatHistory::getConversationId)
                            .collectList();
                })
                .block());
    }

    /**
     * 根据会话ID查找消息列表
     *
     * @param conversationId 会话ID，不能为空
     * @return 指定会话中的消息列表，不会为null
     */
    @NotNull
    @Override
    public List<Message> findByConversationId(@NotNull String conversationId) {
        // 构建异步查询流程：获取用户ID -> 查询聊天历史 -> 解析消息列表
        Mono<List<Message>> resultMono = getUserIdMono()
                .flatMap(userId -> {
                    // 构造查询条件：根据用户ID和会话ID查询聊天历史
                    Query query = new Query(
                            Criteria.where("userId").is(userId)
                                    .and("conversationId").is(conversationId)
                    );
                    // 执行查询并处理结果
                    return mongoTemplate.findOne(query, ChatHistory.class)
                            .flatMap(chatHistory -> {
                                // 如果聊天历史为空，返回空消息列表
                                if (chatHistory == null) {
                                    return Mono.just(new ArrayList<Message>());
                                }
                                List<String> messageStrings = chatHistory.getMessages();
                                // 如果消息字符串列表为空，返回空消息列表
                                if (messageStrings == null || messageStrings.isEmpty()) {
                                    return Mono.just(new ArrayList<Message>());
                                }
                                // 将消息字符串列表转换为Message对象列表
                                return Flux.fromIterable(messageStrings)
                                        .flatMap(messageString -> {
                                            try {
                                                // 将JSON字符串反序列化为Message对象
                                                Message message = objectMapper.readValue(messageString, Message.class);
                                                return Mono.just(message);
                                            } catch (JsonProcessingException e) {
                                                // 如果反序列化失败，跳过该消息
                                                return Mono.empty();
                                            }
                                        })
                                        .collectList();
                            })
                            .switchIfEmpty(Mono.just(new ArrayList<>()));
                });
        // 阻塞等待异步操作完成并获取结果
        List<Message> result = resultMono.block();
        return Objects.requireNonNull(result, "findByConversationId returned null after block()");
    }


    /**
     * 保存对话消息历史记录
     *
     * @param conversationId 对话ID，用于标识特定的对话历史
     * @param messages       消息列表，包含需要保存的所有消息对象
     */
    @Override
    public void saveAll(@NotNull String conversationId, @NotNull List<Message> messages) {
        getUserIdMono()
                .flatMap(userId -> {
                    Query query = new Query(
                            Criteria.where("userId").is(userId)
                                    .and("conversationId").is(conversationId)
                    );
                    // 将消息对象列表转换为JSON字符串列表
                    List<String> messageStrings = messages.stream()
                            .map(message -> {
                                try {
                                    return objectMapper.writeValueAsString(message);
                                } catch (JsonProcessingException e) {
                                    throw new RuntimeException("将Message序列成String失败！", e);
                                }
                            })
                            .toList();
                    Update update = new Update()
                            .set("messages", messageStrings);
                    return mongoTemplate.upsert(query, update, ChatHistory.class);
                })
                .block();
    }


    /**
     * 根据会话ID删除聊天历史记录
     *
     * @param conversationId 会话ID，不能为空
     */
    @Override
    public void deleteByConversationId(@NotNull String conversationId) {
        // 获取用户ID并执行删除操作
        getUserIdMono()
                .flatMap(userId -> {
                    // 构造查询条件：匹配当前用户ID和指定会话ID
                    Query query = new Query(
                            Criteria.where("userId").is(userId)
                                    .and("conversationId").is(conversationId)
                    );
                    // 执行删除操作
                    return mongoTemplate.remove(query, ChatHistory.class);
                })
                .block();
    }


    /**
     * 获取用户ID的Mono包装对象
     * 该方法通过userContextProvider获取当前用户ID，并将其包装在Mono中返回。
     * 如果获取用户ID过程中发生异常，则返回默认值0L。
     *
     * @return Mono<Long> 包含用户ID的响应式对象，正常情况下返回实际用户ID，异常时返回0L
     */
    private Mono<Long> getUserIdMono() {
        // 通过Callable创建Mono，处理用户ID获取逻辑
        return Mono.fromCallable(() -> {
            Long userId;
            try {
                // 尝试从用户上下文提供者获取用户ID
                userId = userContextProvider.getUserId();
            } catch (Exception e) {
                // 发生异常时使用默认用户ID 0L
                userId = 0L;
            }
            return userId;
        });
    }

}
