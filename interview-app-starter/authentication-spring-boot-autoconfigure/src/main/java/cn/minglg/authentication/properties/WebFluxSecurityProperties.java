package cn.minglg.authentication.properties;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ClassName:WebFluxSecurityProperties
 * Package:cn.minglg.authentication.properties
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/16
 * @Version 1.0
 */
@ConfigurationProperties(prefix = "interview.security.webflux")
@Data
public class WebFluxSecurityProperties {
    /**
     * token有效时间过期时间（分）
     */
    private Integer authenticationExpirationMinutes = 30;
    /**
     * jwt密钥
     */
    private String jwtSecretKey = "kX7$!aT9@qW5sE6*RzP";
    /**
     * 登录认证信息存储在redis中的key前缀
     */
    private String authKeyPrefix = "user:security:login";
    /**
     * 不会被JWT拦截的请求路径（白名单，支持通配符表达式）
     */
    private List<String> whiteListPatterns = List.of();
    /**
     * 不会被JWT拦截的请求路径(RequestMatcher格式)
     */
    private RequestMatcher whiteListPatternsAsRequestMatcher;

    /**
     * 初始化白名单模式的请求匹配器
     * 该方法将白名单模式列表转换为Spring Security的OrRequestMatcher对象，
     * 用于匹配符合白名单规则的请求路径。如果白名单模式为空，则使用空列表。
     */
    public void initWhiteListPatternsAsRequestMatcher() {
        // 确保白名单模式不为null，如果为null则初始化为空列表
        whiteListPatterns = whiteListPatterns == null ? Collections.emptyList() : whiteListPatterns;
        // 将白名单模式列表转换为AntPathRequestMatcher对象列表，并包装为OrRequestMatcher
        this.whiteListPatternsAsRequestMatcher =
                new OrRequestMatcher(this.getWhiteListPatterns()
                        .stream()
                        .map(AntPathRequestMatcher::new)
                        .collect(Collectors.toList()));
    }


    /**
     * 在属性注入后执行该方法，确保相关属性被正确注入
     */
    @PostConstruct
    public void initProperties() {
        this.initWhiteListPatternsAsRequestMatcher();
    }
}
