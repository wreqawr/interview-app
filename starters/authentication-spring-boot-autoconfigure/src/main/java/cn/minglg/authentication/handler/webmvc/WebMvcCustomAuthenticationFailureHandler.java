package cn.minglg.authentication.handler.webmvc;

import cn.minglg.authentication.utils.JsonUtils;
import cn.minglg.commons.model.response.GenericResponse;
import cn.minglg.commons.model.response.ResponseCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

/**
 * ClassName:WebMvcCustomAuthenticationFailureHandler
 * Package:cn.minglg.authentication.handler
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/7/11
 * @Version 1.0
 */
public class WebMvcCustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    /**
     * 身份验证尝试失败时调用。
     *
     * @param request   发生身份验证尝试的请求。
     * @param response  响应。
     * @param exception 为拒绝身份验证而引发的异常请求。
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        GenericResponse<?> result = GenericResponse.builder().code(ResponseCode.AUTH_FAIL.getCode()).message("登录失败：" + exception.getMessage()).build();
        response.getWriter().write(JsonUtils.toJsonStr(result));
    }
}
