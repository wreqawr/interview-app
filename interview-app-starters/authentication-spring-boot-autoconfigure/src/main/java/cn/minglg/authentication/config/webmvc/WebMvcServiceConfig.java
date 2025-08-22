package cn.minglg.authentication.config.webmvc;

import cn.hutool.captcha.generator.CodeGenerator;
import cn.minglg.authentication.properties.WebMvcSecurityProperties;
import cn.minglg.authentication.service.CaptchaService;
import cn.minglg.authentication.service.RsaService;
import cn.minglg.authentication.service.impl.CaptchaServiceImpl;
import cn.minglg.authentication.service.impl.RsaServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.security.KeyPair;

/**
 * ClassName:WebMvcServiceConfig
 * Package:cn.minglg.authentication.config.webmvc
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/16
 * @Version 1.0
 */
@RequiredArgsConstructor
@Configuration
public class WebMvcServiceConfig {
    private final WebMvcSecurityProperties securityProperties;
    private final CodeGenerator codeGenerator;
    private final StringRedisTemplate redisTemplate;
    private final KeyPair keyPair;

    @Bean
    @ConditionalOnMissingBean
    public CaptchaService captchaService() {
        return new CaptchaServiceImpl(securityProperties, codeGenerator, redisTemplate);
    }

    @Bean
    @ConditionalOnMissingBean
    public RsaService rsaService() {
        return new RsaServiceImpl(securityProperties, keyPair);
    }
}
