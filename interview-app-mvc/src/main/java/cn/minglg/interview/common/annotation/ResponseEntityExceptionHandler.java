package cn.minglg.interview.common.annotation;

import cn.minglg.interview.common.constant.response.ResponseCode;
import org.springframework.core.annotation.AliasFor;
import org.springframework.http.HttpStatus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author kfzx-minglg
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ResponseEntityExceptionHandler {
    @AliasFor("errResponseCode")
    ResponseCode value() default ResponseCode.OTHER_EXCEPTION;

    @AliasFor("value")
    ResponseCode errResponseCode() default ResponseCode.OTHER_EXCEPTION;

    String errorMessagePrefix() default "";
    
    /**
     * HTTP状态码，默认200
     */
    HttpStatus httpStatus() default HttpStatus.OK;
    
    /**
     * 是否记录异常日志，默认true
     */
    boolean logError() default true;
}
