package cn.minglg.ai.handler;

import cn.minglg.ai.assistant.exception.AssistantCallException;
import cn.minglg.commons.model.response.GenericResponse;
import cn.minglg.commons.model.response.ResponseCode;
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
        return doHandle(e, request, ResponseCode.AI_SERVICE_ERROR, "AI服务异常");
    }

    /**
     * 异常处理方法，用于捕获并处理系统中未被特定异常处理器处理的通用异常
     *
     * @param e       异常对象，包含具体的异常信息
     * @param request HTTP请求对象，包含当前请求的相关信息
     * @return ResponseEntity响应实体，包含异常处理结果和状态信息
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e, HttpServletRequest request) {
        // 调用统一异常处理方法，使用默认的错误码和错误信息进行异常处理
        return doHandle(e, request, ResponseCode.OTHER_EXCEPTION, "其它未知错误");
    }

    /**
     * 处理异常并返回统一的响应结果
     *
     * @param e             异常对象
     * @param request       HTTP请求对象
     * @param responseCode  响应码
     * @param messagePrefix 消息前缀
     * @return ResponseEntity类型的响应结果
     */
    private ResponseEntity<?> doHandle(Throwable e, HttpServletRequest request, ResponseCode responseCode, String messagePrefix) {
        // 获取请求路径和异常信息
        String requestURI = request.getRequestURI();
        String eMessage = e.getMessage();

        // 记录错误日志，包括请求路径和异常信息
        log.error("请求路径：{},业务异常：{}", requestURI, eMessage);

        // 构建异常响应结果
        GenericResponse<?> exceptionResult = GenericResponse.builder()
                .code(responseCode.getCode())
                .message(messagePrefix + "：" + eMessage)
                .build();
        return new ResponseEntity<>(exceptionResult, HttpStatus.OK);
    }


}
