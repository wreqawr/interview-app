package cn.minglg.authentication.filter.webmvc;

import cn.minglg.authentication.properties.WebMvcSecurityProperties;
import cn.minglg.authentication.utils.CaptchaUtils;
import cn.minglg.authentication.utils.JsonUtils;
import cn.minglg.commons.model.response.GenericResponse;
import cn.minglg.commons.model.response.ResponseCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

/**
 * ClassName:WebMvcCaptchaFilter
 * Package:cn.minglg.authentication.filter
 * Description:验证码过滤器
 *
 * @Author kfzx-minglg
 * @Create 2025/7/19
 * @Version 1.0
 */
@AllArgsConstructor
public class WebMvcCaptchaFilter extends OncePerRequestFilter {

    private final WebMvcSecurityProperties securityProperties;
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        GenericResponse<?> checkResult = GenericResponse.builder().code(ResponseCode.CAPTCHA_VERIFY_FAIL.getCode()).message("验证码认证失败！").build();
        RequestMatcher requestMatcher = securityProperties.getCaptcha().getEffectivePatternsAsRequestMatcher();
        String captchaRedisKey = null;
        try {
            // 只有需要验证码的请求才会拦截，不需要验证码的请求路径直接放行
            if (requestMatcher.matches(request)) {
                captchaRedisKey = securityProperties.getCaptcha().getRedisKeyPrefix() + ":" + request.getHeader("captchaId");
                String answer = redisTemplate.opsForValue().get(captchaRedisKey);
                // 获取前端传送过来验证码
                String userInputCaptcha = (String) objectMapper.readValue(request.getInputStream(), Map.class).get("captcha");
                boolean verifyResult = CaptchaUtils.verifyCaptcha(userInputCaptcha, answer);

                // 验证成功则放行
                if (verifyResult) {
                    filterChain.doFilter(request, response);
                } else {
                    response.getWriter().write(JsonUtils.toJsonStr(checkResult));
                }

            } else {
                filterChain.doFilter(request, response);
            }
        } catch (Exception e) {
            checkResult.setMessage(this.getClass() + ":" + e.getMessage());
            response.getWriter().write(JsonUtils.toJsonStr(checkResult));
        } finally {
            // 无论验证成功与否，redis中的验证码信息都要失效
            if (captchaRedisKey != null) {
                redisTemplate.delete(captchaRedisKey);
            }
        }
    }
}
