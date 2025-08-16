package cn.minglg.interview.auth.handler;

import cn.minglg.interview.auth.pojo.User;
import cn.minglg.interview.common.constant.response.ResponseCode;
import cn.minglg.interview.common.properties.GlobalProperties;
import cn.minglg.interview.common.response.R;
import cn.minglg.interview.common.utils.JsonUtils;
import cn.minglg.interview.common.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * ClassName:CustomAuthenticationSuccessHandler
 * Package:cn.minglg.interview.handler
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/7/10
 * @Version 1.0
 */
@RequiredArgsConstructor
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final StringRedisTemplate redisTemplate;
    private final GlobalProperties globalProperties;

    /**
     * 在用户成功通过身份验证时调用。
     *
     * @param request        导致身份验证成功的请求
     * @param response       响应
     * @param authentication 身份验证
     *                       身份验证过程。
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        User user = (User) authentication.getPrincipal();
        // 生成JWT令牌
        long expiration = globalProperties.getAuth().getJwtExpirationMinutes();
        String authKey = globalProperties.getAuth().getAuthKeyPrefix() + ":" + user.getUserId();
        String securityKey = globalProperties.getAuth().getJwtSecretKey();
        String token = JwtUtils.createJwt(user, expiration, securityKey);
        // 登录信息保存至redis，并设置过期时间
        redisTemplate.opsForValue().set(authKey, token, expiration, TimeUnit.MINUTES);
        R result = R.builder().code(ResponseCode.OK.getCode()).message("登录成功，欢迎：" + user.getUsername()).build();
        response.setHeader("Authorization", token);
        response.getWriter().write(JsonUtils.toJsonStr(result));
    }
}
