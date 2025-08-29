package cn.minglg.ai.autoconfig;

import cn.minglg.ai.advisors.CommonAdvisor;
import cn.minglg.ai.advisors.CommonAdvisorRepository;
import cn.minglg.ai.config.InMemoryChatMemoryRepositoryConfig;
import cn.minglg.ai.config.MongoChatMemoryRepositoryConfig;
import cn.minglg.ai.config.RedisChatMemoryRepositoryConfig;
import cn.minglg.ai.constant.ChatClientType;
import cn.minglg.ai.context.UserContextProvider;
import cn.minglg.ai.properties.AiProperties;
import cn.minglg.ai.render.CustomMultiCharTemplateRenderer;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.template.TemplateRenderer;
import org.springframework.ai.template.st.StTemplateRenderer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * ClassName:AiAutoConfiguration
 * Package:cn.minglg.ai.autoconfig
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/22
 * @Version 1.0
 */
@EnableConfigurationProperties(AiProperties.class)
@Import({
        InMemoryChatMemoryRepositoryConfig.class,
        MongoChatMemoryRepositoryConfig.class,
        RedisChatMemoryRepositoryConfig.class
})
@AutoConfiguration
public class AiAutoConfiguration {

    /**
     * 创建并配置聊天客户端Bean
     *
     * @param builder                  聊天客户端构建器，用于创建不同配置的聊天客户端实例
     * @param messageChatMemoryAdvisor 消息记忆Advisor，提供聊天记忆功能支持
     * @return 包含两种类型聊天客户端的映射表，键为客户端类型，值为对应的聊天客户端实例
     */
    @Bean
    @ConditionalOnClass(ChatClient.class)
    public Map<ChatClientType, ChatClient> chatClient(ChatClient.Builder builder,
                                                      MessageChatMemoryAdvisor messageChatMemoryAdvisor) {
        // 构建不带记忆功能的基础聊天客户端
        ChatClient chatWithoutMemory = builder
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
        // 构建带记忆功能的聊天客户端
        ChatClient chatWithMemory = builder
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .defaultAdvisors(messageChatMemoryAdvisor)
                .build();
        return Map.of(
                ChatClientType.GENERAL_WITHOUT_MEMORY, chatWithoutMemory,
                ChatClientType.GENERAL_WITH_MEMORY, chatWithMemory
        );
    }


    /**
     * 创建并配置模板渲染器Bean
     *
     * @param aiProperties AI配置属性对象，用于获取模板分隔符配置
     * @return 配置好的TemplateRenderer实例
     */
    @Bean("stTemplateRenderer")
    @ConditionalOnProperty(
            name = {"interview.ai.start-delimiter-character", "interview.ai.end-delimiter-character"},
            matchIfMissing = true)
    public TemplateRenderer templateRenderer(AiProperties aiProperties) {
        // 获取模板开始和结束分隔符字符配置
        char startDelimiterToken = aiProperties.getStartDelimiterCharacter();
        char endDelimiterToken = aiProperties.getEndDelimiterCharacter();

        // 构建并返回StTemplateRenderer实例
        return StTemplateRenderer.builder()
                .startDelimiterToken(startDelimiterToken)
                .endDelimiterToken(endDelimiterToken)
                .build();
    }

    /**
     * 创建自定义模板渲染器Bean
     *
     * @param aiProperties AI配置属性，用于获取模板解析的开始和结束分隔符
     * @return 自定义多字符模板渲染器实例
     */
    @Bean("customTemplateRenderer")
    @ConditionalOnMissingBean(name = "stTemplateRenderer")
    public TemplateRenderer customTemplateRenderer(AiProperties aiProperties) {
        // 设置模板解析的开始和结束分隔符
        String startDelimiter = aiProperties.getStartDelimiterString();
        String endDelimiter = aiProperties.getEndDelimiterString();

        // 构建并返回自定义多字符模板渲染器
        return CustomMultiCharTemplateRenderer.builder()
                .startDelimiter(startDelimiter)
                .endDelimiter(endDelimiter)
                .build();
    }

    /**
     * 创建聊天记忆实例的工厂方法
     *
     * @param aiProperties         AI配置属性，用于获取最大聊天消息数量限制
     * @param chatMemoryRepository 聊天记忆存储库，用于持久化聊天记录
     * @return 配置好聊天记忆实例
     */
    @Bean
    @ConditionalOnMissingBean(ChatMemory.class)
    public ChatMemory chatMemory(AiProperties aiProperties, ChatMemoryRepository chatMemoryRepository) {
        // 构建消息窗口聊天记忆实例，设置存储库和最大消息数量限制
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(chatMemoryRepository)
                .maxMessages(aiProperties.getMaxChatMessages())
                .build();
    }


    /**
     * 创建消息聊天内存Advisor Bean
     *
     * @param chatMemory 聊天内存实例，用于存储和管理聊天历史记录
     * @return MessageChatMemoryAdvisor 消息聊天内存拦截器实例
     */
    @Bean
    @ConditionalOnMissingBean(MessageChatMemoryAdvisor.class)
    @ConditionalOnBean(ChatMemory.class)
    public MessageChatMemoryAdvisor chatMemoryAdvisor(ChatMemory chatMemory) {
        return MessageChatMemoryAdvisor.builder(chatMemory).build();
    }


    /**
     * 创建并返回一个UserContextProvider实例
     * 该方法用于在Spring容器中注册UserContextProvider Bean，当容器中不存在同类型的Bean时才会创建
     * 返回的UserContextProvider实现了一个简单的用户上下文提供者，始终返回用户ID为0L
     *
     * @return UserContextProvider 用户上下文提供者实例
     */
    @Bean
    @ConditionalOnMissingBean
    public UserContextProvider userContextProvider() {
        return () -> 0L;
    }

        /**
     * 创建并配置通用Advisor列表的Bean
     * 
     * @param advisorRepositoryList 通用AdvisorRepository列表，用于创建对应的Advisor实例
     * @return 按照顺序排序的通用Advisor列表
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(CommonAdvisorRepository.class)
    public List<CommonAdvisor> commonAdvisor(List<CommonAdvisorRepository> advisorRepositoryList) {
        // 创建Advisor列表，基于Repository列表初始化Advisor实例
        List<CommonAdvisor> advisorList = new ArrayList<>();
        for (CommonAdvisorRepository advisorRepository : advisorRepositoryList) {
            advisorList.add(new CommonAdvisor(advisorRepository));
        }
        
        // 按照Advisor的order值进行排序
        advisorList.sort(Comparator.comparingInt(CommonAdvisor::getOrder));
        return advisorList;
    }


}
