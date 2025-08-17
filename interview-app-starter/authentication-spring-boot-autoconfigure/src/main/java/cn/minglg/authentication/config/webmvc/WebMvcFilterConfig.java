package cn.minglg.authentication.config.webmvc;

import cn.minglg.authentication.filter.CaptchaFilter;
import cn.minglg.authentication.filter.CustomAuthenticationFilter;
import cn.minglg.authentication.filter.JwtTokenFilter;
import cn.minglg.authentication.filter.RequestBodyCacheFilter;
import cn.minglg.authentication.handler.CustomAuthenticationFailureHandler;
import cn.minglg.authentication.handler.CustomAuthenticationSuccessHandler;
import cn.minglg.authentication.properties.WebMvcSecurityProperties;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@Configuration
public class WebMvcFilterConfig {
    private final WebMvcSecurityProperties securityProperties;
    private final StringRedisTemplate redisTemplate;
    private final KeyPair keyPair;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    /**
     * 创建验证码过滤器Bean
     *
     * @return CaptchaFilter 验证码过滤器实例
     */
    @Bean
    @ConditionalOnMissingBean
    public CaptchaFilter getCaptchaFilter() {
        return new CaptchaFilter(securityProperties, redisTemplate);
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
     * @param authenticationManager 认证管理器，用于处理认证逻辑
     * @return 配置完成的自定义认证过滤器实例
     */
    @Bean
    @ConditionalOnMissingBean
    public CustomAuthenticationFilter customAuthenticationFilter(AuthenticationManager authenticationManager) {
        // 创建自定义认证过滤器实例
        CustomAuthenticationFilter filter = new CustomAuthenticationFilter(securityProperties, keyPair, authenticationManager);
        // 设置认证成功处理器
        filter.setAuthenticationSuccessHandler(customAuthenticationSuccessHandler);
        // 设置认证失败处理器
        filter.setAuthenticationFailureHandler(customAuthenticationFailureHandler);
        return filter;
    }


    /**
     * 创建JWT令牌过滤器Bean
     *
     * @return JwtTokenFilter JWT令牌过滤器实例
     * 条件说明：
     * - 仅在容器中不存在JwtTokenFilter类型的Bean时才会创建
     * - 该过滤器用于处理JWT令牌的验证和解析
     */
    @Bean
    @ConditionalOnMissingBean
    public JwtTokenFilter jwtTokenFilter() {
        return new JwtTokenFilter(securityProperties, redisTemplate);
    }


    /**
     * 创建请求体缓存过滤器Bean
     * 该方法用于创建RequestBodyCacheFilter实例并注册为Spring Bean。
     * 通过@ConditionalOnMissingBean注解确保只有在容器中不存在该Bean时才会创建，
     * 避免重复注册导致的冲突。
     *
     * @return RequestBodyCacheFilter 请求体缓存过滤器实例
     */
    @Bean
    @ConditionalOnMissingBean
    public RequestBodyCacheFilter requestBodyCacheFilter() {
        return new RequestBodyCacheFilter();
    }

}
