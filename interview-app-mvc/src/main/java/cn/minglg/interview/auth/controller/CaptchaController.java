package cn.minglg.interview.auth.controller;

import cn.hutool.captcha.ICaptcha;
import cn.minglg.interview.auth.service.CaptchaService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

/**
 * ClassName:CaptchaController
 * Package:cn.minglg.interview.auth.controller
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/7/18
 * @Version 1.0
 */
@RequiredArgsConstructor
@RestController
public class CaptchaController {
    private final CaptchaService captchaService;

    @GetMapping("/api/auth/captcha")
    public void getCaptcha(HttpServletResponse response) throws IOException {
        response.setContentType("image/jpeg");
        Map<String, Object> captcha = captchaService.generateCaptcha();
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            String captchaId = (String) captcha.get("captchaId");
            ICaptcha captchaImage = (ICaptcha) captcha.get("captchaImage");
            response.setHeader("captchaId", captchaId);
            captchaImage.write(outputStream);
        }
    }
}
