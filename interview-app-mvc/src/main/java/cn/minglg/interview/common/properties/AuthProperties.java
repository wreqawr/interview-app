package cn.minglg.interview.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * ClassName:AuthProperties
 * Package:cn.minglg.interview.auth.properties
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/7/18
 * @Version 1.0
 */
@Component
@ConfigurationProperties(prefix = "global.auth")
@Data
public class AuthProperties {
    /**
     * 登录页
     */
    private String loginUri;
    /**
     * 退出页
     */
    private String logoutUri;
    /**
     * 登录认证信息存储在redis中的key前缀
     */
    private String authKeyPrefix;

    /**
     * 登录请求超时时间（秒）
     */
    private long requestTimeoutSeconds;
    /**
     * jwt/redis过期时间（分）
     */
    private long jwtExpirationMinutes;
}
