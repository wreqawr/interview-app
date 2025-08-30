package cn.minglg.authentication.config.webmvc;

import cn.minglg.authentication.context.RequestScopedUserContext;
import cn.minglg.authentication.filter.webmvc.WebMvcCaptchaFilter;
import cn.minglg.authentication.filter.webmvc.WebMvcCustomAuthenticationFilter;
import cn.minglg.authentication.filter.webmvc.WebMvcJwtTokenFilter;
import cn.minglg.authentication.filter.webmvc.WebMvcRequestBodyCacheFilter;
import cn.minglg.authentication.handler.webmvc.WebMvcCustomAuthenticationFailureHandler;
import cn.minglg.authentication.handler.webmvc.WebMvcCustomAuthenticationSuccessHandler;
import cn.minglg.authentication.properties.WebMvcSecurityProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import java.security.KeyPair;

/**
 * ClassName:WebMvcFilterConfig
 * Package:cn.minglg.authentication.config.webmvc
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/16
 * @Version 1.0
 */
@Configuration
@ConditionalOnClass(StringRedisTemplate.class)
public class WebMvcFilterConfig {

    /**
     * 创建并配置验证码过滤器Bean
     *
     * @param securityProperties 安全配置属性，用于获取验证码相关配置
     * @param redisTemplate      Redis模板，用于存储和验证验证码
     * @return 配置好的WebMvcCaptchaFilter实例
     */
    @Bean
    @ConditionalOnMissingBean
    public WebMvcCaptchaFilter getCaptchaFilter(WebMvcSecurityProperties securityProperties,
                                                StringRedisTemplate redisTemplate) {
        return new WebMvcCaptchaFilter(securityProperties, redisTemplate);
    }


    /**
     * 创建并配置认证管理器Bean
     *
     * @param http HttpSecurity对象，用于获取共享的AuthenticationManagerBuilder实例
     * @return AuthenticationManager认证管理器实例
     * @throws Exception 构建过程中可能抛出的异常
     */
    @Bean
    @ConditionalOnMissingBean
    public AuthenticationManager authenticationManagerBean(HttpSecurity http) throws Exception {
        // 从HttpSecurity中获取共享的AuthenticationManagerBuilder对象并构建认证管理器
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
    }


    /**
     * 创建自定义认证过滤器Bean
     *
     * @param authenticationManager                    认证管理器，用于处理认证逻辑
     * @param securityProperties                       安全配置属性，包含认证相关的配置信息
     * @param keyPair                                  密钥对，用于认证过程中的加密解密操作
     * @param webMvcCustomAuthenticationSuccessHandler 认证成功处理器，处理认证成功后的逻辑
     * @param webMvcCustomAuthenticationFailureHandler 认证失败处理器，处理认证失败后的逻辑
     * @return 配置完成的WebMvcCustomAuthenticationFilter实例
     */
    @Bean
    @ConditionalOnMissingBean
    public WebMvcCustomAuthenticationFilter customAuthenticationFilter(AuthenticationManager authenticationManager,
                                                                       WebMvcSecurityProperties securityProperties,
                                                                       KeyPair keyPair,
                                                                       WebMvcCustomAuthenticationSuccessHandler webMvcCustomAuthenticationSuccessHandler,
                                                                       WebMvcCustomAuthenticationFailureHandler webMvcCustomAuthenticationFailureHandler) {
        // 创建自定义认证过滤器实例
        WebMvcCustomAuthenticationFilter filter = new WebMvcCustomAuthenticationFilter(securityProperties, keyPair, authenticationManager);
        // 设置认证成功处理器
        filter.setAuthenticationSuccessHandler(webMvcCustomAuthenticationSuccessHandler);
        // 设置认证失败处理器
        filter.setAuthenticationFailureHandler(webMvcCustomAuthenticationFailureHandler);
        return filter;
    }

    /**
     * 创建JWT令牌过滤器Bean
     *
     * @param securityProperties WebMvc安全配置属性，用于获取JWT相关的配置信息
     * @param redisTemplate      Redis模板，用于操作Redis中的令牌黑名单等数据
     * @return WebMvcJwtTokenFilter JWT令牌过滤器实例
     */
    @Bean
    @ConditionalOnMissingBean
    public WebMvcJwtTokenFilter jwtTokenFilter(WebMvcSecurityProperties securityProperties,
                                               StringRedisTemplate redisTemplate,
                                               RequestScopedUserContext userContext) {
        return new WebMvcJwtTokenFilter(securityProperties, redisTemplate, userContext);
    }


    /**
     * 创建请求体缓存过滤器Bean
     * 该方法用于创建RequestBodyCacheFilter实例并注册为Spring Bean。
     * 通过@ConditionalOnMissingBean注解确保只有在容器中不存在该Bean时才会创建，
     * 避免重复注册导致的冲突。
     *
     * @return WebMvcRequestBodyCacheFilter 请求体缓存过滤器实例
     */
    @Bean
    @ConditionalOnMissingBean
    public WebMvcRequestBodyCacheFilter requestBodyCacheFilter() {
        return new WebMvcRequestBodyCacheFilter();
    }

    /**
     * 创建并返回一个请求作用域的用户上下文Bean
     * 该方法用于创建RequestScopedUserContext实例，并将其注册为Spring容器中的Bean。
     * 通过@ConditionalOnMissingBean注解确保只有在容器中不存在同类型Bean时才会创建此Bean，
     * 避免重复定义导致的冲突。
     *
     * @return RequestScopedUserContext 请求作用域的用户上下文实例
     */
    @Bean
    @ConditionalOnMissingBean
    public RequestScopedUserContext requestScopedUserContext() {
        return new RequestScopedUserContext();
    }

}
