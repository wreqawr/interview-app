package cn.minglg.interview.auth.config;

import cn.hutool.captcha.generator.CodeGenerator;
import cn.hutool.captcha.generator.MathGenerator;
import cn.hutool.captcha.generator.RandomGenerator;
import cn.minglg.interview.common.properties.GlobalProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ClassName:CaptchaConfig
 * Package:cn.minglg.interview.auth.config
 * Description: 验证码生成器配置类
 *
 * @Author kfzx-minglg
 * @Create 2025/7/18
 * @Version 1.0
 */
@Configuration
@RequiredArgsConstructor
public class CaptchaGeneratorConfig {

    private final GlobalProperties globalProperties;

    /**
     * 随机字符验证码生成器
     *
     * @return 随机字符验证码生成器
     */
    @Bean
    @ConditionalOnProperty(
            name = "global.captcha.code-generator",
            havingValue = "cn.hutool.captcha.generator.RandomGenerator"
    )
    public CodeGenerator randomCodeGenerator() {
        String baseStr = globalProperties.getCaptcha().getBaseStr();
        Integer codeCount = globalProperties.getCaptcha().getCodeCount();
        return new RandomGenerator(baseStr, codeCount);
    }

    /**
     * 四则运算验证码生成器
     *
     * @return 四则运算验证码生成器
     */
    @Bean
    @ConditionalOnProperty(
            name = "global.captcha.code-generator",
            havingValue = "cn.hutool.captcha.generator.MathGenerator",
            matchIfMissing = true
    )
    public CodeGenerator mathGenerator() {
        return new MathGenerator();
    }
}
