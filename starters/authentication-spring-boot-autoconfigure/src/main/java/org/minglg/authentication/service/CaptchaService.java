package org.minglg.authentication.service;

import java.util.Map;

/**
 * ClassName:CaptchaService
 * Package:cn.minglg.authentication.service
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/7/18
 * @Version 1.0
 */
public interface CaptchaService {
    /**
     * 生成验证码
     *
     * @return 验证码
     */
    Map<String, Object> generateCaptcha();

}
