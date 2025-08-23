package cn.minglg.interview.ai.config;

import cn.minglg.ai.context.UserContextProvider;
import cn.minglg.authentication.utils.UserUtils;
import cn.minglg.interview.common.constant.task.TaskType;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.template.TemplateRenderer;
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
@Configuration
public class AiConfig {

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

    @Bean
    public UserContextProvider userContextProvider() {
        return () -> UserUtils.getCurrentUser().getUserId();
    }

}
