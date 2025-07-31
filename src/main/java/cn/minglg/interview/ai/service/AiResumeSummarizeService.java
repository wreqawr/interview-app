package cn.minglg.interview.ai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * ClassName:AiResumeSummarizeService
 * Package:cn.minglg.interview.ai.service
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/7/31
 * @Version 1.0
 */
@Service
public class AiResumeSummarizeService {
    private final ChatClient chatClient;

    public AiResumeSummarizeService(@Qualifier("resumeSummarize") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String resumeSummarize(String content) {
        return chatClient
                .prompt(content)
                .call()
                .content();
    }
}
