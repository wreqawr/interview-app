package cn.minglg.authentication.config.webmvc;

import cn.hutool.captcha.generator.CodeGenerator;
import cn.minglg.authentication.properties.WebMvcSecurityProperties;
import cn.minglg.authentication.service.CaptchaService;
import cn.minglg.authentication.service.RsaService;
import cn.minglg.authentication.service.impl.CaptchaServiceImpl;
import cn.minglg.authentication.service.impl.RsaServiceImpl;
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
@Configuration
public class WebMvcServiceConfig {

    /**
     * 创建验证码服务Bean实例
     *
     * @param securityProperties 安全配置属性，用于获取验证码相关配置
     * @param codeGenerator      验证码生成器，用于生成验证码字符
     * @param redisTemplate      Redis模板，用于存储和验证验证码
     * @return 验证码服务实现类实例
     */
    @Bean
    @ConditionalOnMissingBean
    public CaptchaService captchaService(WebMvcSecurityProperties securityProperties,
                                         CodeGenerator codeGenerator,
                                         StringRedisTemplate redisTemplate) {
        return new CaptchaServiceImpl(securityProperties, codeGenerator, redisTemplate);
    }


    /**
     * 创建RSA服务Bean实例
     *
     * @param securityProperties Web安全配置属性，用于获取RSA相关的安全配置信息
     * @param keyPair            密钥对，包含RSA公钥和私钥，用于加密解密操作
     * @return RsaService RSA服务实现类实例，提供RSA加密解密功能
     */
    @Bean
    @ConditionalOnMissingBean
    public RsaService rsaService(WebMvcSecurityProperties securityProperties,
                                 KeyPair keyPair) {
        return new RsaServiceImpl(securityProperties, keyPair);
    }

}
