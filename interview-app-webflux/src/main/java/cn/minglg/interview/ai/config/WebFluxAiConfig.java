package cn.minglg.interview.ai.config;

import cn.minglg.ai.advisors.ReactiveRoundLimitManager;
import cn.minglg.ai.context.UserContextProvider;
import cn.minglg.authentication.context.RequestScopedUserContext;
import cn.minglg.authentication.exception.UnKnowUserException;
import cn.minglg.commons.model.task.TaskType;
import cn.minglg.interview.ai.advisor.InterviewRoundAdvisorRepository;
import cn.minglg.interview.ai.properties.InterviewRoundLimitProperties;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.template.TemplateRenderer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName:WebFluxAiConfig
 * Package:cn.minglg.interview.ai.config
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/24
 * @Version 1.0
 */
@Configuration
public class WebFluxAiConfig {
    /**
     * 创建并返回一个UserContextProvider实例
     * 该Provider用于获取当前用户的用户ID
     *
     * @return UserContextProvider 返回一个Lambda表达式实现的UserContextProvider，
     * 其getContext方法返回当前用户的用户ID
     */
    @Bean
    public UserContextProvider userContextProvider(RequestScopedUserContext userContext) {
        return () -> {
            Long userId = userContext.getUser().getUserId();
            if (userId != null) {
                return userId;
            }
            throw new UnKnowUserException("未知用户！");
        };
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
        String systemPromptForComprehensiveAssessment = "/prompt/resume/综合评估-HR.st";
        String systemPromptForMockInterviewStart = "/prompt/interview/基于简历内容的模拟面试问答（开始）.st";
        String systemPromptForMockInterviewStop = "/prompt/interview/基于简历内容的模拟面试问答（结束）.st";
        String systemPromptForMockInterview = "/prompt/interview/面试官角色预设.st";
        String systemPromptForGeneralChat = "/prompt/general/通用聊天.st";


        // 将任务类型与对应的提示词资源进行映射
        map.put(TaskType.COMPREHENSIVE_ASSESSMENT, new ClassPathResource(systemPromptForComprehensiveAssessment));
        map.put(TaskType.MOCK_INTERVIEW_START, new ClassPathResource(systemPromptForMockInterviewStart));
        map.put(TaskType.MOCK_INTERVIEW_STOP, new ClassPathResource(systemPromptForMockInterviewStop));
        map.put(TaskType.MOCK_INTERVIEW, new ClassPathResource(systemPromptForMockInterview));
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
     * 创建InterviewRoundAdvisorRepository Bean实例
     * 该Bean依赖于InterviewRoundLimitProperties和ReactiveRoundLimitManager Bean的存在
     *
     * @param promptTemplateMap 任务类型与提示模板的映射关系，用于获取Mock面试停止的提示模板
     * @param properties        面试轮次限制配置属性
     * @param roundLimitManager 响应式轮次限制管理器
     * @return InterviewRoundAdvisorRepository实例
     */
    @Bean
    @ConditionalOnBean({InterviewRoundLimitProperties.class, ReactiveRoundLimitManager.class})
    public InterviewRoundAdvisorRepository roundAdvisorRepository(Map<TaskType, PromptTemplate> promptTemplateMap,
                                                                  InterviewRoundLimitProperties properties,
                                                                  ReactiveRoundLimitManager roundLimitManager) {
        // 从提示模板映射中获取Mock面试停止任务对应的提示模板
        PromptTemplate promptTemplate = promptTemplateMap.get(TaskType.MOCK_INTERVIEW_STOP);
        // 创建并返回InterviewRoundAdvisorRepository实例
        return new InterviewRoundAdvisorRepository(promptTemplate, properties, roundLimitManager);
    }

    /**
     * 创建ReactiveRedisTemplate实例，用于处理String类型键和Integer类型值的Redis操作
     *
     * @param factory ReactiveRedisConnectionFactory连接工厂实例
     * @return 配置好的ReactiveRedisTemplate<String, Integer>实例
     */
    @Bean
    @ConditionalOnMissingBean
    public ReactiveRedisTemplate<String, Integer> reactiveIntegerRedisTemplate(
            ReactiveRedisConnectionFactory factory) {

        StringRedisSerializer keySerializer = new StringRedisSerializer();
        // 数值序列化，使用字符串表示，简单直观
        GenericToStringSerializer<Integer> valueSerializer = new GenericToStringSerializer<>(Integer.class);

        // 构建Redis序列化上下文，配置键值的序列化方式
        RedisSerializationContext<String, Integer> context = RedisSerializationContext
                .<String, Integer>newSerializationContext(keySerializer)
                .key(keySerializer)
                .value(valueSerializer)
                .hashKey(keySerializer)
                .hashValue(valueSerializer)
                .build();

        return new ReactiveRedisTemplate<>(factory, context);
    }


}
