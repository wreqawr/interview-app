package cn.minglg.ai.advisors;

import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;

/**
 * ClassName:RoundLimitRepository
 * Package:cn.minglg.ai.advisors
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/27
 * @Version 1.0
 */
public interface RoundLimitRepository {
    String TASK_TYPE_STRING = "round_limit_task_type";

    /**
     * 修改当前对话请求
     *
     * @param chatClientRequest 原始请求
     * @param taskTypeString    当前请求任务类型
     * @return 修改后的请求
     */
    ChatClientRequest modifyChatClientRequest(ChatClientRequest chatClientRequest, String taskTypeString);

    /**
     * 修改聊天客户端响应对象
     * 默认不做任何操作，后续加入项目有需求可重写此方法
     *
     * @param chatClientResponse 要修改的聊天客户端响应对象
     * @param taskTypeString     当前请求任务类型
     * @return 修改后的聊天客户端响应对象
     */
    default ChatClientResponse modifyChatClientResponse(ChatClientResponse chatClientResponse, String taskTypeString) {
        return chatClientResponse;
    }

}
