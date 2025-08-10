package cn.minglg.interview.common.properties;

import jakarta.annotation.PostConstruct;
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
 * ClassName:GlobalProperties
 * Package:cn.minglg.interview.properties
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/7/13
 * @Version 1.0
 */
@Component
@ConfigurationProperties(prefix = "global")
@Data
public class GlobalProperties {
    private AuthProperties auth;
    private List<String> whiteListPatterns;
    private RequestMatcher whiteListPatternsAsRequestMatcher;
    private CaptchaProperties captcha;
    private RegisterProperties register;
    private ResumeProperties resume;
    private MinioProperties minio;
    private AsyncProperties async;
    private AiProperties ai;


    public void initWhiteListPatternsAsRequestMatcher() {
        whiteListPatterns = whiteListPatterns == null ? Collections.emptyList() : whiteListPatterns;
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
        this.getCaptcha().initEffectivePatternsAsRequestMatcher();
    }
}
