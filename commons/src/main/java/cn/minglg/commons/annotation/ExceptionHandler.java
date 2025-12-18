package cn.minglg.commons.annotation;

import cn.minglg.commons.model.response.ResponseCode;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author kfzx-minglg
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExceptionHandler {
    @AliasFor("errResponseCode")
    ResponseCode value() default ResponseCode.OTHER_EXCEPTION;

    @AliasFor("value")
    ResponseCode errResponseCode() default ResponseCode.OTHER_EXCEPTION;

    String errorMessagePrefix() default "";

}
