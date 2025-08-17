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
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
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
@RequiredArgsConstructor
@AutoConfiguration
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
public class WebMvcSecurityAutoConfiguration {
    /**
     * 登录认证成功，但是对应controller无访问权限自定义处理器
     */
    private final WebMvcCustomAccessDeniedHandler webMvcCustomAccessDeniedHandler;
    /**
     * 退出成功自定义处理器（前提是处于登录状态）
     */
    private final WebMvcCustomLogoutSuccessHandler webMvcCustomLogoutSuccessHandler;
    /**
     * 全局配置信息
     */
    private final WebMvcSecurityProperties securityProperties;
    /**
     * JWT登录认证过滤器
     */
    private final WebMvcJwtTokenFilter jwtTokenFilter;
    /**
     * 验证码过滤器
     */
    private final WebMvcCaptchaFilter captchaFilter;
    /**
     * 包装request的filter
     */
    private final WebMvcRequestBodyCacheFilter webMvcRequestBodyCacheFilter;


    @Bean
    public CorsConfigurationSource configurationSource() {
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();

        //跨域配置
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        //允许任何来源，http://localhost:10492/
        corsConfiguration.setAllowedOrigins(List.of("*"));
        //允许任何请求方法，post、get、put、delete
        corsConfiguration.setAllowedMethods(List.of("*"));
        //允许任何的请求头 (jwt)
        corsConfiguration.setAllowedHeaders(List.of("*"));
        // 暴露响应头
        corsConfiguration.setExposedHeaders(List.of("captchaId", "Authorization"));

        //注册跨域配置
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return urlBasedCorsConfigurationSource;
    }

    /**
     * 配置Spring Security的安全过滤器链
     *
     * @param http                       HttpSecurity对象，用于配置安全策略
     * @param webMvcCustomAuthenticationFilter 自定义认证过滤器，用于替换默认的UsernamePasswordAuthenticationFilter
     * @param configurationSource        CORS配置源，用于处理跨域请求
     * @return 配置完成的SecurityFilterChain对象
     * @throws Exception 配置过程中可能抛出的异常
     */
    @Bean("webMvcSecurityFilterChain")
    @ConditionalOnMissingBean
    public SecurityFilterChain webMvcSecurityFilterChain(HttpSecurity http, WebMvcCustomAuthenticationFilter webMvcCustomAuthenticationFilter, CorsConfigurationSource configurationSource) throws Exception {
        return http
                // 关闭CSRF防护
                .csrf(AbstractHttpConfigurer::disable)
                // 配置跨域资源共享(CORS)
                .cors(cors ->
                        cors.configurationSource(configurationSource))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()// 显式放行所有OPTIONS
                        .requestMatchers(securityProperties.getWhiteListPatterns().toArray(new String[0])).permitAll()  // 白名单内请求，无需认证
                        .anyRequest().authenticated() // 其他所有请求走认证
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
