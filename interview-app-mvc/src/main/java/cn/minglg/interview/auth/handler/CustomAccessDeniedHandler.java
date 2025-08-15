package cn.minglg.interview.auth.handler;

import cn.hutool.json.JSONUtil;
import cn.minglg.interview.common.constant.response.ResponseCode;
import cn.minglg.interview.common.response.R;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * ClassName:CustomAccessDeniedHandler
 * Package:cn.minglg.interview.handler
 * Description: 权限不足处理器
 *
 * @Author kfzx-minglg
 * @Create 2025/7/13
 * @Version 1.0
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    /**
     * 处理访问被拒绝失败。
     *
     * @param request               请求
     * @param response，以便将失败通知用户代理
     * @param accessDeniedException accessDeniedException
     * @throws IOException      IOException 的情况下
     * @throws ServletException ServletException
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        R result = R.builder().code(ResponseCode.PERMISSION_DENY.getCode()).message("权限不足！").build();
        response.getWriter().write(JSONUtil.toJsonStr(result));
    }
}
