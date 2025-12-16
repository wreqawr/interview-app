package cn.minglg.authentication.autoconfig;

import cn.minglg.authentication.config.webflux.WebFluxFilterConfig;
import cn.minglg.authentication.config.webflux.WebFluxHandlerConfig;
import cn.minglg.authentication.filter.webflux.WebFluxJwtTokenFilter;
import cn.minglg.authentication.properties.WebFluxSecurityProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.savedrequest.NoOpServerRequestCache;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.DispatcherHandler;

import java.util.List;

/**
 * ClassName:WebFluxSecurityAutoConfiguration
 * Package:cn.minglg.authentication.config
 * Description:WebFlux场景下的自动配置类 - 轻量级安全配置
 * 适用于实时性要求高的接口，如在线聊天等
 *
 * @Author kfzx-minglg
 * @Create 2025/8/16
 * @Version 1.0
 */
@EnableConfigurationProperties(WebFluxSecurityProperties.class)
@ConditionalOnClass({DispatcherHandler.class})
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Import({WebFluxFilterConfig.class, WebFluxHandlerConfig.class})
@AutoConfiguration
public class WebFluxSecurityAutoConfiguration {

    /**
     * 配置WebFlux跨域
     */
    @Bean
    public CorsConfigurationSource webfluxCorsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // 允许任何来源
        corsConfiguration.setAllowedOrigins(List.of("*"));
        // 允许任何请求方法
        corsConfiguration.setAllowedMethods(List.of("*"));
        // 允许任何请求头
        corsConfiguration.setAllowedHeaders(List.of("*"));
        // 暴露响应头
        corsConfiguration.setExposedHeaders(List.of("Authorization"));

        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    /**
     * 配置WebFlux安全过滤器链
     * 轻量级配置：只包含基础认证、权限拒绝处理和跨域
     */
    @Bean("webFluxSecurityFilterChain")
    public SecurityWebFilterChain webFluxSecurityFilterChain(ServerHttpSecurity http,
                                                             WebFluxSecurityProperties securityProperties,
                                                             WebFluxJwtTokenFilter jwtTokenFilter,
                                                             ServerAccessDeniedHandler accessDeniedHandler) {
        return http
                // 关闭CSRF防护（适用于API接口）
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                // 关闭HTTP Basic认证
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                // 关闭表单登录
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                // 关闭登出
                .logout(ServerHttpSecurity.LogoutSpec::disable)

                // 配置跨域
                .cors(cors -> cors.configurationSource(webfluxCorsConfigurationSource()))

                // 配置无状态会话管理，适用于前后端分离架构
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .requestCache(cache -> cache.requestCache(NoOpServerRequestCache.getInstance()))

                // 配置请求授权
                .authorizeExchange(auth -> {
                            // 显式放行所有OPTIONS请求
                            auth.pathMatchers(HttpMethod.OPTIONS, "/**").permitAll();
                            // 白名单内请求，无需认证
                            List<String> whiteListPatterns = securityProperties.getWhiteListPatterns();
                            if (whiteListPatterns != null && !whiteListPatterns.isEmpty()) {
                                auth.matchers(ServerWebExchangeMatchers.pathMatchers(
                                        whiteListPatterns.toArray(new String[0])
                                )).permitAll();
                            }
                            // 其他所有请求需要认证
                            auth.anyExchange().authenticated();
                        }
                )
                // 添加自定义JWT过滤器
                .addFilterAt(jwtTokenFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                // 添加权限不足处理逻辑
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.accessDeniedHandler(accessDeniedHandler)
                )
                .build();
    }
}
