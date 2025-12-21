package cn.minglg.resume.handler;

import cn.minglg.commons.model.response.GenericResponse;
import cn.minglg.commons.model.response.ResponseCode;
import cn.minglg.resume.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * ClassName:GlobalExceptionHandler
 * Package:cn.minglg.resume.handler
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/12/21
 * @Version 1.0
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 处理简历上传异常的全局异常处理器
     *
     * @param e       简历上传异常对象，包含具体的异常信息
     * @param request HTTP请求对象，用于获取请求相关信息
     * @return ResponseEntity响应实体，包含封装的异常结果和HTTP状态码
     */
    @ExceptionHandler(ResumeUploadException.class)
    public ResponseEntity<?> handleResumeUploadException(ResumeUploadException e, HttpServletRequest request) {
        return doHandle(e, request, ResponseCode.RESUME_UPLOAD_FAIL, "简历上传失败");
    }

    /**
     * 处理简历下载异常的控制器通知方法
     *
     * @param e       异常对象，包含简历下载失败的具体信息
     * @param request HTTP请求对象，包含客户端请求的相关信息
     * @return ResponseEntity响应实体，包含错误码和错误信息
     */
    @ExceptionHandler(ResumeDownloadException.class)
    public ResponseEntity<?> handleResumeDownloadException(ResumeDownloadException e, HttpServletRequest request) {
        return doHandle(e, request, ResponseCode.RESUME_DOWNLOAD_FAIL, "简历下载失败");
    }

    /**
     * 处理简历删除异常的控制器通知方法
     * 当ResumeDeleteException异常被抛出时，该方法会捕获并处理此异常，
     * 返回统一格式的错误响应信息。
     *
     * @param e       ResumeDeleteException异常实例，包含异常的具体信息
     * @param request HTTP请求对象，用于获取请求相关信息
     * @return ResponseEntity响应实体，包含错误码和错误信息
     */
    @ExceptionHandler(ResumeDeleteException.class)
    public ResponseEntity<?> handleResumeDeleteException(ResumeDeleteException e, HttpServletRequest request) {
        // 调用通用异常处理方法，传入异常实例、请求对象、响应码和错误描述
        return doHandle(e, request, ResponseCode.RESUME_DELETE_FAIL, "简历删除失败");
    }


    /**
     * 处理简历预览异常的控制器通知方法
     *
     * @param e       异常对象，包含简历预览过程中发生的错误信息
     * @param request HTTP请求对象，包含客户端请求的相关信息
     * @return ResponseEntity响应实体，包含错误处理结果和相应的状态码
     */
    @ExceptionHandler(ResumePreviewException.class)
    public ResponseEntity<?> handleResumePreviewException(ResumePreviewException e, HttpServletRequest request) {
        // 调用统一异常处理方法，传入简历预览相关的错误码和错误信息
        return doHandle(e, request, ResponseCode.RESUME_PREVIEW_FAIL, "简历预览失败");
    }


    /**
     * 处理简历分析和保存异常的全局异常处理器
     *
     * @param e       异常对象，包含简历分析和保存过程中发生的错误信息
     * @param request HTTP请求对象，包含当前请求的相关信息
     * @return ResponseEntity响应实体，包含错误码和错误信息
     */
    @ExceptionHandler(ResumeAnalyzeAndSaveException.class)
    public ResponseEntity<?> handleResumeAnalyzeAndSaveException(ResumeAnalyzeAndSaveException e, HttpServletRequest request) {
        // 调用通用异常处理方法，返回简历分析失败的错误响应
        return doHandle(e, request, ResponseCode.RESUME_ANALYZE_FAIL, "简历分析异步任务执行失败");
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
     * 处理异常并返回统一格式的响应结果
     *
     * @param e             异常对象
     * @param request       HTTP请求对象
     * @param responseCode  响应状态码枚举
     * @param messagePrefix 消息前缀字符串
     * @return 包含异常信息的ResponseEntity对象
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
