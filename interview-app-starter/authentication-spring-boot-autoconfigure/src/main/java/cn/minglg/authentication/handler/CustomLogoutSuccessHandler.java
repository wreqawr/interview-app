package cn.minglg.authentication.handler;

import cn.minglg.authentication.constant.response.ResponseCode;
import cn.minglg.authentication.pojo.User;
import cn.minglg.authentication.properties.WebMvcSecurityProperties;
import cn.minglg.authentication.response.R;
import cn.minglg.authentication.utils.JsonUtils;
import cn.minglg.authentication.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;

/**
 * ClassName:CustomLogoutSuccessHandler
 * Package:cn.minglg.authentication.handler
 * Description:退出成功执行这个handler
 *
 * @Author kfzx-minglg
 * @Create 2025/7/18
 * @Version 1.0
 */

@AllArgsConstructor
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    private final WebMvcSecurityProperties securityProperties;
    private final StringRedisTemplate redisTemplate;

    /**
     * 处理用户登出成功的回调方法
     *
     * @param request        HTTP请求对象，用于获取请求头信息
     * @param response       HTTP响应对象，用于设置响应内容和状态
     * @param authentication 认证信息对象，包含当前认证用户的信息
     * @throws IOException 当响应写入失败时抛出此异常
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // 获取当前登录用户信息
        String authorization = request.getHeader("Authorization");
        R result = null;
        try {
            String secretKey = securityProperties.getJwtSecretKey();
            User user = JwtUtils.verifyJwt(authorization, secretKey);
            String authKey = securityProperties.getAuthKeyPrefix() + ":" + user.getUserId();
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
