package cn.minglg.ai.advisors;

/**
 * ClassName:RoundLimitManager
 * Package:cn.minglg.ai.advisors
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/12/20
 * @Version 1.0
 */
public interface RoundLimitManager {
    /**
     * 获取当前对话轮数
     *
     * @param conversationId 对话ID
     * @return 当前轮数
     */
    Integer getCurrentRound(String conversationId);

    /**
     * 增加对话轮数
     *
     * @param conversationId 对话ID
     */
    void increaseChatRound(String conversationId);

    /**
     * 重置对话轮数
     *
     * @param conversationId 对话ID
     */
    void resetChatRound(String conversationId);
}
