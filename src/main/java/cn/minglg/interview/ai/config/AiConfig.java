package cn.minglg.interview.ai.config;

import cn.minglg.interview.common.constant.TaskType;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
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
    /**
     * 定制chatClient对象
     *
     * @param builder ChatClient.Builder
     * @return chatClient对象
     */
    @Bean("chat")
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }

    /**
     * 静态系统提示词（当前对话有效）
     *
     * @return 提示词集合
     */
    @Bean
    public Map<TaskType, Resource> systemPromptStaticTemplate() {
        Map<TaskType, Resource> map = new HashMap<>(16);
        String systemPromptForResumeSummarize = "/prompt/resume/简历关键信息提取.st";
        String systemPromptForResumeAnalyze = "/prompt/resume/简历分析-求职者.st";
        String systemPromptForComprehensiveAssessment = "/prompt/resume/综合评估-HR.st";
        map.put(TaskType.RESUME_SUMMARIZE, new ClassPathResource(systemPromptForResumeSummarize));
        map.put(TaskType.RESUME_ANALYZE, new ClassPathResource(systemPromptForResumeAnalyze));
        map.put(TaskType.COMPREHENSIVE_ASSESSMENT, new ClassPathResource(systemPromptForComprehensiveAssessment));
        return map;
    }

    /**
     * 提示词模板（动态提示词，支持占位符）
     *
     * @param systemPrompt 资源对象
     * @return 动态提示词集合
     */
    @Bean
    public Map<TaskType, PromptTemplate> systemPromptDynamicTemplate(Map<TaskType, Resource> systemPrompt) {
        Map<TaskType, PromptTemplate> map = new HashMap<>(16);
        for (TaskType taskType : systemPrompt.keySet()) {
            Resource resource = systemPrompt.get(taskType);
            PromptTemplate promptTemplate = new PromptTemplate(resource);
            map.put(taskType, promptTemplate);
        }
        return map;
    }
}
