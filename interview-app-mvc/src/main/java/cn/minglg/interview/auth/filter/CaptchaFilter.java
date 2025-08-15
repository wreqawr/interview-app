package cn.minglg.interview.auth.filter;

import cn.minglg.interview.common.constant.response.ResponseCode;
import cn.minglg.interview.common.properties.GlobalProperties;
import cn.minglg.interview.common.response.R;
import cn.minglg.interview.common.utils.CaptchaUtils;
import cn.minglg.interview.common.utils.JsonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

/**
 * ClassName:CaptchaFilter
 * Package:cn.minglg.interview.auth.filter
 * Description:验证码过滤器
 *
 * @Author kfzx-minglg
 * @Create 2025/7/19
 * @Version 1.0
 */
@RequiredArgsConstructor
@Component
public class CaptchaFilter extends OncePerRequestFilter {

    private final GlobalProperties globalProperties;
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();



    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, HttpServletResponse response, @NotNull FilterChain filterChain) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        R checkResult = R.builder().code(ResponseCode.CAPTCHA_VERIFY_FAIL.getCode()).message("验证码认证失败！").build();
        RequestMatcher requestMatcher = this.globalProperties.getCaptcha().getEffectivePatternsAsRequestMatcher();
        String captchaRedisKey = null;
        try {
            // 只有需要验证码的请求才会拦截，不需要验证码的请求路径直接放行
            if (requestMatcher.matches(request)) {
                captchaRedisKey = globalProperties.getCaptcha().getRedisKeyPrefix() + ":" + request.getHeader("captchaId");
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
            checkResult.setMessage(e.getMessage());
            response.getWriter().write(JsonUtils.toJsonStr(checkResult));
        } finally {
            // 无论验证成功与否，redis中的验证码信息都要失效
            if (captchaRedisKey != null) {
                redisTemplate.delete(captchaRedisKey);
            }
        }

    }
}
