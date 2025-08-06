package cn.minglg.interview.ai.config;

import cn.minglg.interview.common.constant.TaskType;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.Map;

/**
 * ClassName:AiConfig
 * Package:cn.minglg.interview.ai.config
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/7/31
 * @Version 1.0
 */
@RequiredArgsConstructor
@Configuration
public class AiConfig {

    @Bean("chat")
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }

    @Bean
    public Map<TaskType, Resource> systemPrompt() {
        String systemPromptForResumeSummarize = "/prompt/ResumeSummarize.txt";
        return Map.of(TaskType.RESUME_SUMMARIZE, new ClassPathResource(systemPromptForResumeSummarize));
    }
}
