package org.minglg.ai.repository.webmvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.jetbrains.annotations.NotNull;
import org.minglg.ai.context.UserContextProvider;
import org.minglg.ai.deserializer.MessageDeserializer;
import org.minglg.ai.pojo.ChatHistory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.messages.Message;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName:MongoChatMemoryRepository
 * Package:cn.minglg.ai.repository.webmvc
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/23
 * @Version 1.0
 */
public class MongoChatMemoryRepository implements ChatMemoryRepository {

    private final MongoTemplate mongoTemplate;
    private final UserContextProvider userContextProvider;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public MongoChatMemoryRepository(MongoTemplate mongoTemplate,
                                     UserContextProvider userContextProvider) {
        this.mongoTemplate = mongoTemplate;
        this.userContextProvider = userContextProvider;
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Message.class, new MessageDeserializer());
        this.objectMapper.registerModule(module);
    }

    /**
     * 查找当前用户的所有会话ID
     *
     * @return 包含所有会话ID的字符串列表，不允许为null
     */
    @NotNull
    @Override
    public List<String> findConversationIds() {
        // 构造查询条件，根据用户ID筛选聊天记录
        Query query = new Query(
                Criteria.where("userId").is(userContextProvider.getUserId())
        );
        // 查询聊天记录并提取会话ID列表
        return mongoTemplate.find(query, ChatHistory.class)
                .stream()
                .map(ChatHistory::getConversationId)
                .toList();
    }


    /**
     * 根据会话ID查找消息列表
     *
     * @param conversationId 会话ID，用于标识特定的对话记录
     * @return 返回与指定会话ID相关联的消息列表
     */
    @NotNull
    @Override
    public List<Message> findByConversationId(@NotNull String conversationId) {
        Query query = new Query(
                Criteria.where("userId").is(userContextProvider.getUserId())
                        .and("conversationId").is(conversationId)
        );
        List<Message> messages = new ArrayList<>();
        ChatHistory chatHistory = mongoTemplate.findOne(query, ChatHistory.class);
        // 如果找到了聊天历史，将其中的字符串消息反序列化为Message对象
        if (chatHistory != null) {
            messages = chatHistory.getMessages()
                    .stream()
                    .map(messageString -> {
                        try {
                            return objectMapper.readValue(messageString, Message.class);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException("将mongo中的消息反序列成Message对象失败！", e);
                        }
                    })
                    .toList();
        }
        return messages;
    }

    /**
     * 保存指定对话的所有消息记录
     *
     * @param conversationId 对话ID，用于标识特定的对话记录
     * @param messages       消息对象列表，包含需要保存的所有消息
     */
    @Override
    public void saveAll(@NotNull String conversationId, @NotNull List<Message> messages) {
        Query query = new Query(
                Criteria.where("userId").is(userContextProvider.getUserId())
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
        // 更新用户和对话ID对应的消息记录
        mongoTemplate.upsert(query, update, ChatHistory.class);

    }

    /**
     * 根据对话ID删除聊天历史记录
     *
     * @param conversationId 对话ID，用于标识需要删除的聊天记录
     */
    @Override
    public void deleteByConversationId(@NotNull String conversationId) {
        // 根据当前用户ID和对话ID删除对应的聊天历史记录
        Query query = new Query(
                Criteria.where("userId").is(userContextProvider.getUserId())
                        .and("conversationId").is(conversationId)
        );
        mongoTemplate.remove(query, ChatHistory.class);
    }

}
