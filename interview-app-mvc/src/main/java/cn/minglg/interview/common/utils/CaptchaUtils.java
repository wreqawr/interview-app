package cn.minglg.interview.common.utils;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ICaptcha;
import cn.hutool.captcha.generator.CodeGenerator;
import cn.hutool.captcha.generator.MathGenerator;
import cn.hutool.core.math.Calculator;
import cn.minglg.interview.common.properties.GlobalProperties;

import java.util.Map;

/**
 * ClassName:CaptchaUtils
 * Package:cn.minglg.interview.utils
 * Description:验证码工具类
 *
 * @Author kfzx-minglg
 * @Create 2025/7/18
 * @Version 1.0
 */
public class CaptchaUtils {
    public static Map<String, Object> generateCaptcha(GlobalProperties globalProperties, CodeGenerator codeGenerator) {
        int width = globalProperties.getCaptcha().getWidth();
        int height = globalProperties.getCaptcha().getHeight();
        int thickness = globalProperties.getCaptcha().getThickness();
        ICaptcha captcha = CaptchaUtil.createGifCaptcha(width, height, codeGenerator, thickness);
        captcha.createCode();
        String answer = captcha.getCode();
        if (codeGenerator instanceof MathGenerator) {
            answer = String.valueOf((int) Calculator.conversion(captcha.getCode()));
        }
        return Map.of("captcha", captcha, "answer", answer);
    }

    public static boolean verifyCaptcha(String userInputCaptcha, String answer) {
        if (answer == null) {
            return false;
        }
        return answer.equalsIgnoreCase(userInputCaptcha);
    }


}
