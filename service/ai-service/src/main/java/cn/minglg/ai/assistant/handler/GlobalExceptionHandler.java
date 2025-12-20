package cn.minglg.ai.assistant.handler;

import cn.minglg.ai.assistant.exception.AssistantCallException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * ClassName:GlobalExceptionHandler
 * Package:cn.minglg.ai.assistant.handler
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/12/19
 * @Version 1.0
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理助手调用异常的全局异常处理器
     *
     * @param e       异常对象，包含具体的错误信息
     * @param request HTTP请求对象，用于获取请求相关信息
     * @return ResponseEntity响应实体，包含错误信息和HTTP状态码
     */
    @ExceptionHandler(AssistantCallException.class)
    public ResponseEntity<?> handleAssistantCallException(AssistantCallException e, HttpServletRequest request) {
        // 获取请求路径和异常信息
        String requestURI = request.getRequestURI();
        String eMessage = e.getMessage();

        // 记录错误日志，包括请求路径和异常信息
        log.error("请求路径：{},业务异常：{}", requestURI, eMessage);

        // 返回400状态码和异常信息
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(eMessage);
    }

}
