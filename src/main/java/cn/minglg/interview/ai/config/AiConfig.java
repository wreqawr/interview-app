package cn.minglg.interview.ai.config;

import cn.minglg.interview.common.constant.TaskType;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.HashMap;
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
        Map<TaskType, Resource> map = new HashMap<>(16);
        String systemPromptForResumeSummarize = "/prompt/简历关键信息提取.txt";
        String systemPromptForResumeAnalyze = "/prompt/简历分析-求职者.txt";
        String systemPromptForComprehensiveAssessment = "/prompt/综合评估-HR.txt";
        map.put(TaskType.RESUME_SUMMARIZE, new ClassPathResource(systemPromptForResumeSummarize));
        map.put(TaskType.RESUME_ANALYZE, new ClassPathResource(systemPromptForResumeAnalyze));
        map.put(TaskType.COMPREHENSIVE_ASSESSMENT, new ClassPathResource(systemPromptForComprehensiveAssessment));
        return map;
    }
}
