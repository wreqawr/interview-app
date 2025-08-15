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
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.KeyPair;

/**
 * ClassName:CustomLogoutSuccessHandler
 * Package:cn.minglg.interview.auth.handler
 * Description:退出成功执行这个handler
 *
 * @Author kfzx-minglg
 * @Create 2025/7/18
 * @Version 1.0
 */

@RequiredArgsConstructor
@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    private final StringRedisTemplate redisTemplate;
    private final GlobalProperties globalProperties;
    private final KeyPair keyPair;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // 获取当前登录用户信息
        String authorization = request.getHeader("Authorization");
        R result = null;
        try {
            User user = JwtUtils.verifyJwt(authorization, keyPair);
            String authKey = globalProperties.getAuth().getAuthKeyPrefix() + ":" + user.getUserId();
            // 删除redis中的登录信息
            redisTemplate.delete(authKey);
            response.setContentType("application/json;charset=UTF-8");
            result = R.builder().code(ResponseCode.OK.getCode()).message("账号：" + user.getUsername() + "退出成功！").build();
        } catch (Exception e) {
            result = R.builder().code(ResponseCode.LOGOUT_FAIL.getCode()).message("账号退出失败，原因为：" + e.getMessage()).build();
        } finally {
            response.getWriter().write(JsonUtils.toJsonStr(result));
        }
    }
}
