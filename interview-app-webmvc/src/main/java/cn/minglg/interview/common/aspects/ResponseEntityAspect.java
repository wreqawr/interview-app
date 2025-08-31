package cn.minglg.interview.common.aspects;

import cn.minglg.authentication.response.R;
import cn.minglg.interview.common.annotation.ExceptionHandler;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * ClassName:ResponseEntityAspect
 * Package:cn.minglg.interview.common.aspects
 * Description:响应结果包装类
 *
 * @Author kfzx-minglg
 * @Create 2025/8/3
 * @Version 1.0
 */
@Aspect
@Component
public class ResponseEntityAspect {
    /**
     * controller层增强方法
     *
     * @param handler 自定义注解对象
     * @return 封装的响应数据
     */
    @Around("@annotation(handler)")
    public ResponseEntity<?> responseHandler(ProceedingJoinPoint pjp, ExceptionHandler handler) {
        Integer errorCode = handler.errResponseCode().getCode();
        String errorMessagePrefix = handler.errorMessagePrefix() + "，原因为：";
        try {
            return (ResponseEntity<?>) pjp.proceed();
        } catch (Throwable e) {
            R exceptionResult = R.builder()
                    .code(errorCode)
                    .message(errorMessagePrefix + e.getMessage())
                    .build();
            return new ResponseEntity<>(exceptionResult, HttpStatus.OK);
        }
    }
}
