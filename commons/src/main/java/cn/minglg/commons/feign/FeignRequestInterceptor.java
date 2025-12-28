package cn.minglg.commons.feign;

import cn.minglg.commons.async.AsyncContextHolder;
import cn.minglg.commons.model.constants.Constants;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
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
        String token = AsyncContextHolder.getAttribute(Constants.AUTHORIZATION_TOKEN_KEY);
        // 如果获取到 token，添加到请求头
        if (token != null) {
            log.info("子线程接收到的token是: {}", token);
            requestTemplate.header("Authorization", token);
            return;
        }
        // 从RequestContextHolder中获取请求属性并提取token
        if (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes) {
            token = attributes.getRequest().getHeader(Constants.AUTHORIZATION_TOKEN_KEY);
            log.info("从RequestContextHolder获取到token: {}", token);
            requestTemplate.header("Authorization", token);
        }
    }


}
