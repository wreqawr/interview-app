package cn.minglg.authentication.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * ClassName:WebFluxSecurityProperties
 * Package:cn.minglg.authentication.properties
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/16
 * @Version 1.0
 */
@ConfigurationProperties(prefix = "interview.webflux.security")
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
    private List<String> whiteListPatterns = null;
}
