package cn.minglg.interview.ai.config;

import cn.minglg.interview.ai.render.CustomMultiCharTemplateRenderer;
import cn.minglg.interview.common.constant.TaskType;
import cn.minglg.interview.common.properties.GlobalProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.template.TemplateRenderer;
import org.springframework.ai.template.st.StTemplateRenderer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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

    private final GlobalProperties globalProperties;

    /**
     * 定制chatClient对象（带上下文记忆）
     *
     * @param builder ChatClient.Builder
     * @param advisor 上下文记忆拦截器
     * @return chatClient对象
     */
    @Bean("chatWithMemory")
    @Primary
    public ChatClient chatClientWithMemory(ChatClient.Builder builder, MessageChatMemoryAdvisor advisor) {
        return builder
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .defaultAdvisors(advisor)
                .build();
    }

    /**
     * 定制chatClient对象（不带上下文记忆）
     *
     * @param builder ChatClient.Builder
     * @return chatClient对象
     */
    @Bean("chatWithoutMemory")
    public ChatClient chatClientWithoutMemory(ChatClient.Builder builder) {
        ChatOptions options = ChatOptions.builder()
                .temperature(1.2)
                .build();
        return builder
                //.defaultOptions(options)
                .defaultSystem("你是面试官，负责进行技术面试。每次对话都要记住这是同一场面试的延续。")
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }

    /**
     * 自定义模板渲染器的占位符为<>（默认是{}）
     *
     * @return 模板渲染器对象
     */
    @Bean("stTemplateRenderer")
    public TemplateRenderer templateRenderer() {
        char startDelimiterToken = '<';
        char endDelimiterToken = '>';
        return StTemplateRenderer.builder()
                .startDelimiterToken(startDelimiterToken)
                .endDelimiterToken(endDelimiterToken)
                .build();
    }

    /**
     * 功能更强大的自定义模板渲染器，支持任意字符串占位符
     *
     * @return 模板渲染器对象
     */
    @Bean("customTemplateRenderer")
    public TemplateRenderer customTemplateRenderer() {
        String startDelimiter = "$#{";
        String endDelimiter = "}";
        return CustomMultiCharTemplateRenderer.builder()
                .startDelimiter(startDelimiter)
                .endDelimiter(endDelimiter)
                .build();
    }

    /**
     * 系统提示词资源（当前对话有效）
     *
     * @return 提示词集合
     */
    @Bean
    public Map<TaskType, Resource> resourceMap() {
        Map<TaskType, Resource> map = new HashMap<>(16);
        String systemPromptForResumeSummarize = "/prompt/resume/简历关键信息提取.st";
        String systemPromptForResumeAnalyze = "/prompt/resume/简历分析-求职者.st";
        String systemPromptForComprehensiveAssessment = "/prompt/resume/综合评估-HR.st";
        String systemPromptForMockInterview = "/prompt/interview/基于简历内容的模拟面试问答.st";
        map.put(TaskType.RESUME_SUMMARIZE, new ClassPathResource(systemPromptForResumeSummarize));
        map.put(TaskType.RESUME_ANALYZE, new ClassPathResource(systemPromptForResumeAnalyze));
        map.put(TaskType.COMPREHENSIVE_ASSESSMENT, new ClassPathResource(systemPromptForComprehensiveAssessment));
        map.put(TaskType.MOCK_INTERVIEW, new ClassPathResource(systemPromptForMockInterview));
        return map;
    }

    /**
     * 提示词模板（动态提示词，支持占位符）
     *
     * @param resourceMap 资源对象集合
     * @return 动态提示词集合
     */
    @Bean
    public Map<TaskType, PromptTemplate> systemPromptDynamicTemplate(Map<TaskType, Resource> resourceMap, @Qualifier("stTemplateRenderer") TemplateRenderer templateRenderer) {
        Map<TaskType, PromptTemplate> map = new HashMap<>(16);
        for (TaskType taskType : resourceMap.keySet()) {
            Resource resource = resourceMap.get(taskType);
            PromptTemplate promptTemplate = PromptTemplate.builder()
                    .resource(resource)
                    .renderer(templateRenderer)
                    .build();
            map.put(taskType, promptTemplate);
        }
        return map;
    }

    @Bean
    public ChatMemory chatMemory(ChatMemoryRepository chatMemoryRepository) {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(chatMemoryRepository)
                .maxMessages(globalProperties.getAi().getMaxChatMessages())
                .build();
    }

    @Bean
    public MessageChatMemoryAdvisor chatMemoryAdvisor(ChatMemory chatMemory) {
        return MessageChatMemoryAdvisor.builder(chatMemory).build();
    }
}
