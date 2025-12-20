package cn.minglg.interview.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * ClassName:FeignRequestInterceptor
 * Package:cn.minglg.resume.config
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/12/19
 * @Version 1.0
 */
@Component
@Slf4j
public class FeignRequestInterceptor implements RequestInterceptor {
    /**
     * 应用请求拦截器方法
     * 该方法从当前请求上下文中获取Authorization头部信息，并将其添加到目标请求中，
     * 用于实现请求头信息的传递和转发
     *
     * @param requestTemplate 请求模板对象，用于构建和修改HTTP请求
     */
    @Override
    public void apply(RequestTemplate requestTemplate) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes attributes) {
            String token = attributes.getRequest().getHeader("Authorization");
            log.info("给feign请求添加token: {}", token);
            requestTemplate.header("Authorization", token);
        }
    }

}
