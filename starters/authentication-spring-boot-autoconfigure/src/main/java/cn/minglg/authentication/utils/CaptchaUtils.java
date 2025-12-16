package cn.minglg.authentication.utils;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ICaptcha;
import cn.hutool.captcha.generator.CodeGenerator;
import cn.hutool.captcha.generator.MathGenerator;
import cn.hutool.core.math.Calculator;
import cn.minglg.authentication.properties.WebMvcSecurityProperties;

import java.util.Map;

/**
 * ClassName:CaptchaUtils
 * Package:cn.minglg.authentication.utils
 * Description:验证码工具类
 *
 * @Author kfzx-minglg
 * @Create 2025/7/18
 * @Version 1.0
 */
public class CaptchaUtils {
    /**
     * 生成验证码图片和答案
     *
     * @param securityProperties 安全配置属性，用于获取验证码的宽度、高度和粗细度等配置
     * @param codeGenerator      验证码生成器，用于生成验证码内容
     * @return 包含验证码图片对象和答案的Map，key为"captcha"和"answer"
     */
    public static Map<String, Object> generateCaptcha(WebMvcSecurityProperties securityProperties, CodeGenerator codeGenerator) {
        // 从安全配置中获取验证码的尺寸和粗细度参数
        int width = securityProperties.getCaptcha().getWidth();
        int height = securityProperties.getCaptcha().getHeight();
        int thickness = securityProperties.getCaptcha().getThickness();

        // 创建GIF格式的验证码图片
        ICaptcha captcha = CaptchaUtil.createGifCaptcha(width, height, codeGenerator, thickness);
        captcha.createCode();
        String answer = captcha.getCode();

        // 如果是数学表达式验证码，需要计算出数学表达式的结果作为答案
        if (codeGenerator instanceof MathGenerator) {
            answer = String.valueOf((int) Calculator.conversion(captcha.getCode()));
        }

        return Map.of("captcha", captcha, "answer", answer);
    }


    /**
     * 验证用户输入的验证码是否正确
     *
     * @param userInputCaptcha 用户输入的验证码
     * @param answer           正确的验证码答案
     * @return 如果验证码匹配返回true，否则返回false
     */
    public static boolean verifyCaptcha(String userInputCaptcha, String answer) {
        // 检查答案是否为空
        if (answer == null) {
            return false;
        }
        // 忽略大小写比较验证码
        return answer.equalsIgnoreCase(userInputCaptcha);
    }


}
