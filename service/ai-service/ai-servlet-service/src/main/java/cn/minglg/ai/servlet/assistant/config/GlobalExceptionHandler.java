package cn.minglg.ai.servlet.assistant.config;

import cn.minglg.ai.servlet.assistant.exception.AssistantCallException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * ClassName:GlobalExceptionHandler
 * Package:cn.minglg.ai.servlet.assistant.config
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/12/19
 * @Version 1.0
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(AssistantCallException.class)
    public ResponseEntity<?> handleAssistantCallException(AssistantCallException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String eMessage = e.getMessage();
        log.error("请求路径：{},业务异常：{}", requestURI, eMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(eMessage);
    }
}
