package cn.minglg.interview.auth.handler;

import cn.hutool.json.JSONUtil;
import cn.minglg.interview.common.constant.response.ResponseCode;
import cn.minglg.interview.common.response.R;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * ClassName:CustomAuthenticationFailureHandler
 * Package:cn.minglg.interview.handler
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/7/11
 * @Version 1.0
 */
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    /**
     * 身份验证尝试失败时调用。
     *
     * @param request   发生身份验证尝试的请求。
     * @param response  响应。
     * @param exception 为拒绝身份验证而引发的异常请求。
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        R result = R.builder().code(ResponseCode.AUTH_FAIL.getCode()).message("登录失败：" + exception.getMessage()).build();
        response.getWriter().write(JSONUtil.toJsonStr(result));
    }
}
