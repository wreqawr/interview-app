package cn.minglg.authentication.service.impl;

import cn.hutool.captcha.ICaptcha;
import cn.hutool.captcha.generator.CodeGenerator;
import cn.minglg.authentication.properties.WebMvcSecurityProperties;
import cn.minglg.authentication.service.CaptchaService;
import cn.minglg.authentication.utils.CaptchaUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * ClassName:CaptchaServiceImpl
 * Package:cn.minglg.authentication.service.impl
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/7/18
 * @Version 1.0
 */
@AllArgsConstructor
public class CaptchaServiceImpl implements CaptchaService {
    private final WebMvcSecurityProperties securityProperties;
    private final CodeGenerator codeGenerator;
    private final StringRedisTemplate redisTemplate;


    /**
     * 生成验证码
     *
     * @return 验证码
     */
    @Override
    public Map<String, Object> generateCaptcha() {
        Map<String, Object> generateCaptcha = CaptchaUtils.generateCaptcha(securityProperties, codeGenerator);
        ICaptcha captcha = (ICaptcha) generateCaptcha.get("captcha");
        String answer = (String) generateCaptcha.get("answer");
        String captchaId = String.valueOf(System.currentTimeMillis());
        String captchaRedisKey = securityProperties.getCaptcha().getRedisKeyPrefix() + ":" + captchaId;
        long expireTime = securityProperties.getCaptcha().getCaptchaExpireMinutes();
        redisTemplate.opsForValue().set(captchaRedisKey, answer, expireTime, TimeUnit.MINUTES);
        return Map.of("captchaId", captchaId, "captchaImage", captcha);
    }

}
