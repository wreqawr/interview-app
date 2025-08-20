package cn.minglg.authentication.config.webmvc;

import cn.hutool.captcha.generator.CodeGenerator;
import cn.hutool.captcha.generator.MathGenerator;
import cn.hutool.captcha.generator.RandomGenerator;
import cn.minglg.authentication.properties.WebMvcSecurityProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ClassName:CaptchaGeneratorConfig
 * Package:cn.minglg.authentication.config.webmvc
 * Description:验证码生成器配置类
 *
 * @Author kfzx-minglg
 * @Create 2025/8/16
 * @Version 1.0
 */
@Configuration
public class CaptchaGeneratorConfig {

    /**
     * 随机验证码生成器Bean
     * 当配置属性"interview.security.webmvc.captcha.code-generator"的值为
     * "cn.hutool.captcha.generator.RandomGenerator"且容器中不存在CodeGenerator类型的Bean时，
     * 创建并返回一个RandomGenerator实例。
     *
     * @param securityProperties WebMvc安全配置属性对象，用于获取验证码相关配置
     * @return 验证码生成器实例
     */
    @Bean
    @ConditionalOnProperty(
            name = "interview.security.webmvc.captcha.code-generator",
            havingValue = "cn.hutool.captcha.generator.RandomGenerator"
    )
    @ConditionalOnMissingBean
    @ConditionalOnClass(RandomGenerator.class)
    public CodeGenerator randomCodeGenerator(WebMvcSecurityProperties securityProperties) {
        // 从安全配置属性中获取验证码基础字符集和验证码位数
        String baseStr = securityProperties.getCaptcha().getBaseStr();
        Integer codeCount = securityProperties.getCaptcha().getCodeCount();
        return new RandomGenerator(baseStr, codeCount);
    }


    /**
     * 四则运算验证码生成器
     *
     * @return 四则运算验证码生成器
     */
    @Bean
    @ConditionalOnProperty(
            name = "interview.security.webmvc.captcha.code-generator",
            havingValue = "cn.hutool.captcha.generator.MathGenerator",
            matchIfMissing = true
    )
    @ConditionalOnMissingBean
    @ConditionalOnClass(MathGenerator.class)
    public CodeGenerator mathGenerator() {
        return new MathGenerator();
    }
}
