package cn.minglg.ai.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * ClassName:ChatHistory
 * Package:cn.minglg.ai.pojo
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/23
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "c_chat_history")
// 添加复合索引注解
@CompoundIndex(
        name = "userid_conversationid_idx",
        def = "{'userId': 1, 'conversationId': 1}",
        unique = true
)

public class ChatHistory {
    private Long userId;
    private String conversationId;
    private List<String> messages;
}
