package cn.minglg.authentication.properties;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ClassName:WebMvcSecurityProperties
 * Package:cn.minglg.authentication.properties
 * Description:webmvc安全配置属性
 *
 * @Author kfzx-minglg
 * @Create 2025/8/16
 * @Version 1.0
 */
@ConfigurationProperties(prefix = "interview.webmvc.security")
@Data
public class WebMvcSecurityProperties {
    /**
     * 登录请求超时时间（秒）
     */
    private Integer requestTimeoutSeconds = 5;
    /**
     * token有效时间过期时间（分）
     */
    private Integer authenticationExpirationMinutes = 30;
    /**
     * jwt密钥
     */
    private String jwtSecretKey = "kX7$!aT9@qW5sE6*RzP";
    /**
     * 登录页
     */
    private String loginUri = "/api/user/login";
    /**
     * 退出页
     */
    private String logoutUri = "/api/user/logout";
    /**
     * 登录认证信息存储在redis中的key前缀
     */
    private String authKeyPrefix = "user:security:login";
    /**
     * 不会被JWT拦截的请求路径（白名单，支持通配符表达式）
     */
    private List<String> whiteListPatterns = null;
    /**
     * 不会被JWT拦截的请求路径(RequestMatcher格式)
     */
    private RequestMatcher whiteListPatternsAsRequestMatcher;
    /**
     * 验证码信息
     */
    private Captcha captcha;


    @Data
    public static class Captcha {
        /**
         * 验证码宽度
         */
        private Integer width = 120;
        /**
         * 验证码高度
         */
        private Integer height = 40;
        /**
         * 干扰元素个数
         */
        private Integer thickness = 10;
        /**
         * 验证码数字个数
         */
        private Integer codeCount = 4;
        /**
         * 随机字符序列（当验证码生成器为RandomGenerator时有效，不配置默认为26位大小写字母+10位数字组合）
         */
        private String baseStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        /**
         * 验证码生成器（用于条件注解，不配置默认是cn.hutool.captcha.generator.MathGenerator）
         */
        private String codeGenerator = "cn.hutool.captcha.generator.MathGenerator";
        /**
         * 保存在redis中的key前缀
         */
        private String redisKeyPrefix = "user:security:captcha";
        /**
         * 验证码过期时间
         */
        private Integer captchaExpireMinutes = 5;
        /**
         * 需要使用验证码的请求路径
         */
        private List<String> effectivePatterns = null;
        /**
         * 需要使用验证码的请求路径对应的RequestMatcher
         */
        private RequestMatcher effectivePatternsAsRequestMatcher;

        public void initEffectivePatternsAsRequestMatcher() {
            effectivePatterns = effectivePatterns == null ? Collections.emptyList() : effectivePatterns;
            PathPatternRequestMatcher.Builder builder = PathPatternRequestMatcher.withDefaults();
            this.effectivePatternsAsRequestMatcher =
                    new OrRequestMatcher(this.getEffectivePatterns()
                            .stream()
                            .map(builder::matcher)
                            .collect(Collectors.toList()));
        }
    }

    /**
     * 初始化白名单模式的请求匹配器
     * 该方法将白名单模式列表转换为Spring Security的AntPathRequestMatcher对象列表，
     * 并将其包装为OrRequestMatcher，用于匹配符合任一白名单模式的请求路径。
     * 如果白名单模式列表为null，则将白名单模式请求匹配器设置为null。
     * 否则，将每个白名单模式字符串转换为AntPathRequestMatcher对象，
     * 然后将这些匹配器收集到一个列表中，并使用OrRequestMatcher进行包装。
     */
    public void initWhiteListPatternsAsRequestMatcher() {
        // 确保白名单模式列表不为null，如果为null则初始化为空列表
        if (whiteListPatterns == null) {
            this.whiteListPatternsAsRequestMatcher = null;
        } else {
            // 将白名单模式列表转换为AntPathRequestMatcher对象列表，并包装为OrRequestMatcher
            PathPatternRequestMatcher.Builder builder = PathPatternRequestMatcher.withDefaults();
            this.whiteListPatternsAsRequestMatcher =
                    new OrRequestMatcher(this.getWhiteListPatterns()
                            .stream()
                            .map(builder::matcher)
                            .collect(Collectors.toList()));
        }
    }


    /**
     * 在属性注入后执行该方法，确保相关属性被正确注入
     */
    @PostConstruct
    public void initProperties() {
        this.initWhiteListPatternsAsRequestMatcher();
        if (this.getCaptcha() != null) {
            this.getCaptcha().initEffectivePatternsAsRequestMatcher();
        }
    }
}
