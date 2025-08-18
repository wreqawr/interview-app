package cn.minglg.interview.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * ClassName:AspectTest
 * Package:cn.minglg.interview.aop
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/6
 * @Version 1.0
 */
@Aspect
@Order(Ordered.LOWEST_PRECEDENCE - 1)
@Component
public class AspectTest {

    @Around("@annotation(handler)")
    public Object logAspect(ProceedingJoinPoint pjp, CustomHandler handler) throws Throwable {
        System.out.println("*****自定义代理开始执行");
        Object result = pjp.proceed();
        System.out.println("*****自定义代理结束执行");
        return result;
    }
}
