package cn.minglg.authentication.config.webmvc;

import cn.minglg.authentication.handler.CustomAccessDeniedHandler;
import cn.minglg.authentication.handler.CustomAuthenticationFailureHandler;
import cn.minglg.authentication.handler.CustomAuthenticationSuccessHandler;
import cn.minglg.authentication.handler.CustomLogoutSuccessHandler;
import cn.minglg.authentication.properties.WebMvcSecurityProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;


/**
 * ClassName:WebMvcHandlerConfig
 * Package:cn.minglg.authentication.config.webmvc
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/16
 * @Version 1.0
 */
@RequiredArgsConstructor
@Configuration
public class WebMvcHandlerConfig {
    private final WebMvcSecurityProperties securityProperties;
    private final StringRedisTemplate redisTemplate;

    /**
     * 创建自定义访问拒绝处理器的Bean
     * 该方法用于创建并注册CustomAccessDeniedHandler实例作为Spring Bean。
     * 当容器中不存在同类型的Bean时，才会创建此Bean实例。
     *
     * @return CustomAccessDeniedHandler 自定义访问拒绝处理器实例
     */
    @Bean
    @ConditionalOnMissingBean
    public CustomAccessDeniedHandler customAccessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }


    /**
     * 创建自定义认证失败处理器的Bean
     * 该方法用于创建并注册CustomAuthenticationFailureHandler实例，
     * 当Spring容器中不存在同类型的Bean时才会创建此Bean。
     *
     * @return CustomAuthenticationFailureHandler 认证失败处理器实例
     */
    @Bean
    @ConditionalOnMissingBean
    public CustomAuthenticationFailureHandler customAuthenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }


    /**
     * 创建自定义认证成功处理器Bean
     *
     * @return CustomAuthenticationSuccessHandler 自定义认证成功处理器实例
     * 条件说明：
     * - 仅在容器中不存在CustomAuthenticationSuccessHandler类型的Bean时才会创建
     * - 使用securityProperties和redisTemplate依赖注入来初始化处理器
     */
    @Bean
    @ConditionalOnMissingBean
    public CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler(securityProperties, redisTemplate);
    }

    /**
     * 创建自定义登出成功处理器Bean
     *
     * @return CustomLogoutSuccessHandler 自定义登出成功处理器实例
     * 该方法用于创建并配置自定义的登出成功处理器，当用户登出成功时会调用该处理器
     * 处理器依赖于安全属性配置和Redis模板来进行相关业务逻辑处理
     * 只有在Spring容器中不存在同类型的Bean时才会创建该Bean实例
     */
    @Bean
    @ConditionalOnMissingBean
    public CustomLogoutSuccessHandler customLogoutSuccessHandler() {
        return new CustomLogoutSuccessHandler(securityProperties, redisTemplate);
    }

}
