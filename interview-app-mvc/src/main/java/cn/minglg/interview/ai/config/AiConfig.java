package cn.minglg.interview.ai.config;

import cn.minglg.interview.ai.render.CustomMultiCharTemplateRenderer;
import cn.minglg.interview.common.constant.ai.ChatClientType;
import cn.minglg.interview.common.constant.task.TaskType;
import cn.minglg.interview.common.properties.AiProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.template.TemplateRenderer;
import org.springframework.ai.template.st.StTemplateRenderer;
import org.springframework.beans.factory.annotation.Qualifier;
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

    private final AiProperties aiProperties;

    /**
     * 初始化chatClient集合
     *
     * @param builder 构建器
     * @param advisor 对话拦截器
     * @return chatClient对象
     */
    @Bean
    public Map<ChatClientType, ChatClient> chatClient(ChatClient.Builder builder, MessageChatMemoryAdvisor advisor) {
        // 构建不带记忆的聊天客户端
        ChatClient chatWithoutMemory = builder
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
        // 构建带记忆的聊天客户端
        ChatClient chatWithMemory = builder
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .defaultAdvisors(advisor)
//                .defaultTools(toolService)
                .build();
        return Map.of(
                ChatClientType.GENERAL_WITHOUT_MEMORY, chatWithoutMemory,
                ChatClientType.GENERAL_WITH_MEMORY, chatWithMemory
        );
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
        // 创建任务类型与资源映射关系
        Map<TaskType, Resource> map = new HashMap<>(16);

        // 定义各类任务对应的系统提示词文件路径
        String systemPromptForResumeSummarize = "/prompt/resume/简历关键信息提取.st";
        String systemPromptForResumeAnalyze = "/prompt/resume/简历分析-求职者.st";
        String systemPromptForComprehensiveAssessment = "/prompt/resume/综合评估-HR.st";
        String systemPromptForMockInterviewStart = "/prompt/interview/基于简历内容的模拟面试问答（开始）.st";
        String systemPromptForMockInterviewStop = "/prompt/interview/基于简历内容的模拟面试问答（结束）.st";
        String systemPromptForGeneralChat = "/prompt/general/通用聊天.st";

        // 将任务类型与对应的提示词资源进行映射
        map.put(TaskType.RESUME_SUMMARIZE, new ClassPathResource(systemPromptForResumeSummarize));
        map.put(TaskType.RESUME_ANALYZE, new ClassPathResource(systemPromptForResumeAnalyze));
        map.put(TaskType.COMPREHENSIVE_ASSESSMENT, new ClassPathResource(systemPromptForComprehensiveAssessment));
        map.put(TaskType.MOCK_INTERVIEW_START, new ClassPathResource(systemPromptForMockInterviewStart));
        map.put(TaskType.MOCK_INTERVIEW_STOP, new ClassPathResource(systemPromptForMockInterviewStop));
        map.put(TaskType.GENERAL_CHAT, new ClassPathResource(systemPromptForGeneralChat));
        return map;
    }


    /**
     * 提示词模板（动态提示词，支持占位符）
     *
     * @param resourceMap      资源对象集合
     * @param templateRenderer 模板渲染器
     * @return 动态提示词集合
     */
    @Bean
    public Map<TaskType, PromptTemplate> systemPromptDynamicTemplate(Map<TaskType, Resource> resourceMap, @Qualifier("stTemplateRenderer") TemplateRenderer templateRenderer) {
        // 创建提示词模板映射表
        Map<TaskType, PromptTemplate> map = new HashMap<>(16);
        // 遍历资源对象集合，为每种任务类型构建对应的提示词模板
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


    /**
     * 创建并配置聊天记忆Bean
     *
     * @param chatMemoryRepository 聊天记忆存储库，用于持久化聊天记录
     * @return 配置好的聊天记忆实例，包含消息窗口限制和存储库配置
     */
    @Bean
    public ChatMemory chatMemory(ChatMemoryRepository chatMemoryRepository) {
        // 构建消息窗口聊天记忆实例，设置存储库和最大消息数量限制
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(chatMemoryRepository)
                .maxMessages(aiProperties.getMaxChatMessages())
                .build();
    }


    /**
     * 创建消息聊天内存顾问Bean
     *
     * @param chatMemory 聊天内存实例，用于存储和管理聊天历史记录
     * @return MessageChatMemoryAdvisor 消息聊天内存拦截器实例
     */
    @Bean
    public MessageChatMemoryAdvisor chatMemoryAdvisor(ChatMemory chatMemory) {
        return MessageChatMemoryAdvisor.builder(chatMemory).build();
    }

}
