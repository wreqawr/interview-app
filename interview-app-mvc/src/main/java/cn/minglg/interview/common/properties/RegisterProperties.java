package cn.minglg.interview.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ClassName:RegisterProperties
 * Package:cn.minglg.interview.auth.properties
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/7/20
 * @Version 1.0
 */
@Component
@ConfigurationProperties(prefix = "global.register")
@Data
public class RegisterProperties {
    private String roleRedisKeyPrefix;
    private List<String> notAllowRoles;
}
