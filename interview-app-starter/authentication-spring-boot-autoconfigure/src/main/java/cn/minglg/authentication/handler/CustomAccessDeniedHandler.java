package cn.minglg.authentication.handler;

import cn.minglg.authentication.constant.response.ResponseCode;
import cn.minglg.authentication.response.R;
import cn.minglg.authentication.utils.JsonUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

/**
 * ClassName:CustomAccessDeniedHandler
 * Package:cn.minglg.authentication.handler
 * Description: 权限不足处理器
 *
 * @Author kfzx-minglg
 * @Create 2025/7/13
 * @Version 1.0
 */
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    /**
     * 处理访问被拒绝失败。
     *
     * @param request               请求
     * @param response，以便将失败通知用户代理
     * @param accessDeniedException accessDeniedException
     * @throws IOException IOException 的情况下
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        R result = R.builder().code(ResponseCode.PERMISSION_DENY.getCode()).message("权限不足！").build();
        response.getWriter().write(JsonUtils.toJsonStr(result));
    }
}
