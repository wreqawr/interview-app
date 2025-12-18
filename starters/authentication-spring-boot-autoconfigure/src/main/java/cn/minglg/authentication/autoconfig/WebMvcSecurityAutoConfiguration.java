package cn.minglg.authentication.autoconfig;

import cn.minglg.authentication.config.webmvc.*;
import cn.minglg.authentication.filter.webmvc.WebMvcCaptchaFilter;
import cn.minglg.authentication.filter.webmvc.WebMvcCustomAuthenticationFilter;
import cn.minglg.authentication.filter.webmvc.WebMvcJwtTokenFilter;
import cn.minglg.authentication.filter.webmvc.WebMvcRequestBodyCacheFilter;
import cn.minglg.authentication.handler.webmvc.WebMvcCustomAccessDeniedHandler;
import cn.minglg.authentication.handler.webmvc.WebMvcCustomLogoutSuccessHandler;
import cn.minglg.authentication.properties.WebMvcSecurityProperties;
import jakarta.servlet.Servlet;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.DispatcherServlet;

import java.util.List;

/**
 * ClassName:WebMvcSecurityAutoConfiguration
 * Package:cn.minglg.authentication.config
 * Description:传统WebMvc场景自动配置类
 *
 * @Author kfzx-minglg
 * @Create 2025/8/16
 * @Version 1.0
 */
@EnableConfigurationProperties(WebMvcSecurityProperties.class)
@ConditionalOnClass({Servlet.class, DispatcherServlet.class})
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@EnableMethodSecurity
@EnableWebSecurity
@Import({
        CaptchaGeneratorConfig.class,
        RsaKeyGeneratorConfig.class,
        WebMvcFilterConfig.class,
        WebMvcHandlerConfig.class,
        WebMvcServiceConfig.class
})
@AutoConfiguration
public class WebMvcSecurityAutoConfiguration {


    /**
     * 配置WebMvc安全过滤器链，用于处理认证、授权、跨域、会话管理等安全相关逻辑。
     *
     * @param http                             Spring Security的HttpSecurity对象，用于构建安全配置
     * @param webMvcCustomAuthenticationFilter 自定义认证过滤器，替换默认的用户名密码认证逻辑
     *                                         //     * @param configurationSource              CORS配置源，用于处理跨域请求
     * @param webMvcCustomAccessDeniedHandler  权限不足时的自定义处理逻辑
     * @param webMvcCustomLogoutSuccessHandler 登出成功后的自定义处理逻辑
     * @param securityProperties               安全相关配置属性，如白名单、登出路径等
     * @param jwtTokenFilter                   JWT Token验证过滤器，用于解析和验证请求中的JWT
     * @param captchaFilter                    验证码验证过滤器，用于校验请求中的验证码
     * @param webMvcRequestBodyCacheFilter     请求体缓存过滤器，用于多次读取请求体内容
     * @return 构建完成的SecurityFilterChain实例
     * @throws Exception 配置过程中可能抛出的异常
     */
    @Bean("webMvcSecurityFilterChain")
    public SecurityFilterChain webMvcSecurityFilterChain(HttpSecurity http,
                                                         WebMvcCustomAuthenticationFilter webMvcCustomAuthenticationFilter,
                                                         WebMvcCustomAccessDeniedHandler webMvcCustomAccessDeniedHandler,
                                                         WebMvcCustomLogoutSuccessHandler webMvcCustomLogoutSuccessHandler,
                                                         WebMvcSecurityProperties securityProperties,
                                                         WebMvcJwtTokenFilter jwtTokenFilter,
                                                         WebMvcCaptchaFilter captchaFilter,
                                                         WebMvcRequestBodyCacheFilter webMvcRequestBodyCacheFilter
    ) throws Exception {
        return http
                // 关闭CSRF防护
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                            // 显式放行所有OPTIONS请求
                            auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
                            List<String> whiteListPatterns = securityProperties.getWhiteListPatterns();
                            // 白名单内请求，无需认证
                            if (whiteListPatterns != null && !whiteListPatterns.isEmpty()) {
                                auth.requestMatchers(whiteListPatterns.toArray(new String[0])).permitAll();
                            }
                            // 其他所有请求走认证
                            auth.anyRequest().authenticated();
                        }
                )

                // 配置无状态会话管理，适用于前后端分离架构
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 自定义认证过滤器，替换框架默认的UsernamePasswordAuthenticationFilter
                .addFilterAt(webMvcCustomAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                // 加入request包装过滤器
                .addFilterBefore(webMvcRequestBodyCacheFilter, WebMvcCustomAuthenticationFilter.class)
                // 加入验证码过滤器
                .addFilterBefore(captchaFilter, WebMvcCustomAuthenticationFilter.class)
                // 关键位置：在 CustomAuthenticationFilter之前添加jwtTokenFilter
                .addFilterBefore(jwtTokenFilter, WebMvcCustomAuthenticationFilter.class)

                // 配置权限不足时的处理逻辑
                .exceptionHandling(exceptionHandling -> exceptionHandling.accessDeniedHandler(webMvcCustomAccessDeniedHandler))
                .logout(logout -> logout
                        .logoutUrl(securityProperties.getLogoutUri())
                        .logoutSuccessHandler(webMvcCustomLogoutSuccessHandler))
                .build();
    }


    /**
     * 创建BCrypt密码编码器
     */
    @Bean("passwordEncoder")
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
