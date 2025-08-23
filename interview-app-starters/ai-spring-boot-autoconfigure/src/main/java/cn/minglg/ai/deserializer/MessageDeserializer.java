package cn.minglg.ai.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * ClassName:MessageDeserializer
 * Package:cn.minglg.ai.deserializer
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/23
 * @Version 1.0
 */
@Slf4j
public class MessageDeserializer extends JsonDeserializer<Message> {
    private static final Map<String, Function<String, Message>> MESSAGE_FACTORIES = new ConcurrentHashMap<>();

    static {
        MESSAGE_FACTORIES.put("USER", UserMessage::new);
        MESSAGE_FACTORIES.put("ASSISTANT", AssistantMessage::new);
        MESSAGE_FACTORIES.put("SYSTEM", SystemMessage::new);
    }

    /**
     * 反序列化JSON节点为Message对象
     *
     * @param p    JSON解析器，用于读取和解析JSON数据
     * @param ctxt 反序列化上下文，提供反序列化过程中的上下文信息
     * @return 解析得到的Message对象
     * @throws IOException 当JSON解析或读取过程中发生IO异常时抛出
     */
    @Override
    public Message deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        log.debug("Deserializing message: {}", node);

        // 如果节点是纯文本， 默认创建UserMessage对象
        if (node.isTextual()) {
            return new UserMessage(node.asText());
        }

        // 提取消息类型
        String type = extractMessageType(node);

        // 提取消息内容
        String content = extractContent(node);

        // 根据消息类型创建对应的消息对象
        return Optional.ofNullable(type).map(String::toUpperCase).map(MESSAGE_FACTORIES::get).orElseGet(() -> {
            if (type == null) {
                log.warn("Message type not found, defaulting to USER");
            } else {
                log.warn("Unknown message type: {}, defaulting to USER", type);
            }
            return MESSAGE_FACTORIES.get("USER");
        }).apply(content);
    }

    /**
     * 从JSON节点中提取消息类型信息
     * <p>
     * 该方法按优先级顺序从JSON节点中提取消息类型：
     * 1. 首先尝试获取"messageType"字段
     * 2. 如果不存在，则尝试获取"type"字段
     * 3. 如果都不存在，则尝试获取"role"字段并转换为大写
     *
     * @param node 包含消息信息的JSON节点，不能为空
     * @return 提取到的消息类型字符串，如果所有候选字段都不存在则返回null
     */
    private String extractMessageType(JsonNode node) {
        // 按优先级顺序提取消息类型：messageType -> type -> role(大写)
        return Optional.ofNullable(node.get("messageType"))
                .map(JsonNode::asText)
                .orElseGet(() -> Optional.ofNullable(node.get("type"))
                        .map(JsonNode::asText)
                        .orElseGet(
                                () -> Optional.ofNullable(node.get("role")).map(n -> n.asText().toUpperCase()).orElse(null)));
    }

    /**
     * 从JsonNode中提取内容文本
     *
     * @param node JSON节点对象，用于提取内容
     * @return 提取到的内容文本，如果找不到content或text字段则返回节点的字符串表示
     */
    private String extractContent(JsonNode node) {
        // 首先尝试获取content字段的文本值，如果不存在则尝试获取text字段的文本值，
        // 如果都不存在则返回整个节点的字符串表示
        return Optional.ofNullable(node.get("content"))
                .map(JsonNode::asText)
                .orElseGet(
                        () -> Optional.ofNullable(node.get("text")).map(JsonNode::asText).orElseGet(node::toString));
    }


}
