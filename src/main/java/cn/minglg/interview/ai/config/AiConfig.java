package cn.minglg.interview.ai.config;

import cn.minglg.interview.common.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

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
    private final ResourceLoader resourceLoader;

    @Bean("resumeSummarize")
    public ChatClient chatClient(ChatClient.Builder builder) {
        String promptFile = "classpath:prompt/ResumeSummarize.txt";
        String prompt = FileUtils.readFileFromClassPath(resourceLoader, promptFile);
        return builder
                .defaultSystem(prompt)
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }
}
