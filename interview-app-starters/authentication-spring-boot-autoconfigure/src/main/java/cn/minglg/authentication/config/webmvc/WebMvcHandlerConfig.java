package cn.minglg.authentication.config.webmvc;

import cn.minglg.authentication.handler.webmvc.WebMvcCustomAccessDeniedHandler;
import cn.minglg.authentication.handler.webmvc.WebMvcCustomAuthenticationFailureHandler;
import cn.minglg.authentication.handler.webmvc.WebMvcCustomAuthenticationSuccessHandler;
import cn.minglg.authentication.handler.webmvc.WebMvcCustomLogoutSuccessHandler;
import cn.minglg.authentication.properties.WebMvcSecurityProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
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
@Configuration
@ConditionalOnClass({StringRedisTemplate.class})
public class WebMvcHandlerConfig {
    /**
     * 创建自定义访问拒绝处理器的Bean
     * 该方法用于创建并注册CustomAccessDeniedHandler实例作为Spring Bean。
     * 当容器中不存在同类型的Bean时，才会创建此Bean实例。
     *
     * @return WebMvcCustomAccessDeniedHandler 自定义访问拒绝处理器实例
     */
    @Bean
    @ConditionalOnMissingBean
    public WebMvcCustomAccessDeniedHandler customAccessDeniedHandler() {
        return new WebMvcCustomAccessDeniedHandler();
    }


    /**
     * 创建自定义认证失败处理器的Bean
     * 该方法用于创建并注册CustomAuthenticationFailureHandler实例，
     * 当Spring容器中不存在同类型的Bean时才会创建此Bean。
     *
     * @return WebMvcCustomAuthenticationFailureHandler 认证失败处理器实例
     */
    @Bean
    @ConditionalOnMissingBean
    public WebMvcCustomAuthenticationFailureHandler customAuthenticationFailureHandler() {
        return new WebMvcCustomAuthenticationFailureHandler();
    }


    /**
     * 创建自定义认证成功处理器Bean
     * 该方法用于创建WebMvcCustomAuthenticationSuccessHandler实例，用于处理用户认证成功后的业务逻辑。
     * 只有在Spring容器中不存在同类型的Bean时才会创建此Bean。
     *
     * @param securityProperties 安全配置属性，包含认证成功后的跳转配置等信息
     * @param redisTemplate      Redis模板，用于存储认证成功后的相关数据
     * @return WebMvcCustomAuthenticationSuccessHandler 认证成功处理器实例
     */
    @Bean
    @ConditionalOnMissingBean
    public WebMvcCustomAuthenticationSuccessHandler customAuthenticationSuccessHandler(WebMvcSecurityProperties securityProperties,
                                                                                       StringRedisTemplate redisTemplate) {
        return new WebMvcCustomAuthenticationSuccessHandler(securityProperties, redisTemplate);
    }


    /**
     * 创建自定义登出成功处理器Bean
     *
     * @param securityProperties 安全配置属性，包含登出相关的配置信息
     * @param redisTemplate      Redis模板，用于操作Redis中的用户会话数据
     * @return WebMvcCustomLogoutSuccessHandler 自定义登出成功处理器实例
     */
    @Bean
    @ConditionalOnMissingBean
    public WebMvcCustomLogoutSuccessHandler customLogoutSuccessHandler(WebMvcSecurityProperties securityProperties,
                                                                       StringRedisTemplate redisTemplate) {
        return new WebMvcCustomLogoutSuccessHandler(securityProperties, redisTemplate);
    }


}
