package cn.minglg.interview.auth.service.impl;

import cn.hutool.captcha.ICaptcha;
import cn.hutool.captcha.generator.CodeGenerator;
import cn.minglg.interview.auth.service.CaptchaService;
import cn.minglg.interview.common.properties.GlobalProperties;
import cn.minglg.interview.common.utils.CaptchaUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * ClassName:CaptchaServiceImpl
 * Package:cn.minglg.interview.auth.service.impl
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/7/18
 * @Version 1.0
 */
@RequiredArgsConstructor
@Service
public class CaptchaServiceImpl implements CaptchaService {
    private final GlobalProperties globalProperties;
    private final CodeGenerator codeGenerator;
    private final StringRedisTemplate redisTemplate;


    /**
     * 生成验证码
     *
     * @return 验证码
     */
    @Override
    public Map<String, Object> generateCaptcha() {
        Map<String, Object> generateCaptcha = CaptchaUtils.generateCaptcha(globalProperties, codeGenerator);
        ICaptcha captcha = (ICaptcha) generateCaptcha.get("captcha");
        String answer = (String) generateCaptcha.get("answer");
        String captchaId = String.valueOf(System.currentTimeMillis());
        String captchaRedisKey = globalProperties.getCaptcha().getRedisKeyPrefix() + ":" + captchaId;
        long expireTime = globalProperties.getCaptcha().getRedisKeyExpireMinutes();
        redisTemplate.opsForValue().set(captchaRedisKey, answer, expireTime, TimeUnit.MINUTES);
        return Map.of("captchaId", captchaId, "captchaImage", captcha);
    }

}
