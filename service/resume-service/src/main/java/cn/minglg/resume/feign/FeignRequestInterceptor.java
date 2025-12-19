package cn.minglg.resume.feign;

import cn.minglg.resume.config.AsyncConfig;
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
        // 获取当前请求的RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        String token = null;

        if (requestAttributes != null) {
            // 优先从RequestAttributes的attribute中获取toke（适用于异步线程场景）
            Object tokenObj = requestAttributes.getAttribute(
                    AsyncConfig.AUTHORIZATION_TOKEN_KEY,
                    RequestAttributes.SCOPE_REQUEST
            );
            if (tokenObj instanceof String) {
                token = (String) tokenObj;
            }

            // 如果从 attribute 中获取不到，尝试从 HttpServletRequest 中获取（适用于同步请求场景）
            if (token == null && requestAttributes instanceof ServletRequestAttributes attributes) {
                try {
                    token = attributes.getRequest().getHeader("Authorization");
                } catch (IllegalStateException e) {
                    log.warn("HttpServletRequest可能已被回收（异步线程场景），忽略异常：{}", e.getMessage());
                }
            }
        }

        // 如果获取到 token，添加到请求头
        if (token != null) {
            requestTemplate.header("Authorization", token);
        }
    }

}
