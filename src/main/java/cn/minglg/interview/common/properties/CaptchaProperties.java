package cn.minglg.interview.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ClassName:CaptchaProperties
 * Package:cn.minglg.interview.auth.pojo
 * Description:验证码基础属性
 *
 * @Author kfzx-minglg
 * @Create 2025/7/18
 * @Version 1.0
 */

@Component
@ConfigurationProperties(prefix = "global.captcha")
@Data
public class CaptchaProperties {
    /**
     * 验证码宽度
     */
    private Integer width;
    /**
     * 验证码高度
     */
    private Integer height;
    /**
     * 干扰元素个数
     */
    private Integer thickness;
    /**
     * 验证码字符个数
     */
    private Integer codeCount;
    /**
     * 随机字符序列（当验证码生成器为RandomGenerator时有效，不配置默认为26位大小写字母+10位数字组合）
     */
    private String baseStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    /**
     * 验证码生成器（用于条件注解）
     */
    private String codeGenerator;
    /**
     * 保存在redis中的key前缀
     */
    private String redisKeyPrefix;
    /**
     * redis中保存key的过期时间
     */
    private long redisKeyExpireMinutes;
    /**
     * 需要使用验证码的请求路径
     */
    private List<String> effectivePatterns;
    /**
     * 需要使用验证码的请求路径(RequestMatcher格式)
     */
    private RequestMatcher effectivePatternsAsRequestMatcher;


    public void initEffectivePatternsAsRequestMatcher() {
        effectivePatterns = effectivePatterns == null ? Collections.emptyList() : effectivePatterns;
        this.effectivePatternsAsRequestMatcher =
                new OrRequestMatcher(this.getEffectivePatterns()
                        .stream()
                        .map(AntPathRequestMatcher::new)
                        .collect(Collectors.toList()));
    }
}
