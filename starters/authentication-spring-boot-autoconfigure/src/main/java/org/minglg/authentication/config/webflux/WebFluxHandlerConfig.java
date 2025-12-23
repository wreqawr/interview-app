package org.minglg.authentication.config.webflux;

import org.minglg.authentication.handler.webflux.WebFluxCustomAccessDeniedHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ClassName:WebFluxHandlerConfig
 * Package:cn.minglg.authentication.config.webflux
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/20
 * @Version 1.0
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class WebFluxHandlerConfig {
    /**
     * 创建自定义访问拒绝处理器的Bean
     * 该方法用于创建WebFluxCustomAccessDeniedHandler实例，当用户尝试访问没有权限的资源时，
     * 将使用此处理器来处理访问拒绝异常。
     *
     * @return WebFluxCustomAccessDeniedHandler 自定义的访问拒绝处理器实例
     */
    @Bean
    @ConditionalOnMissingBean
    public WebFluxCustomAccessDeniedHandler customAccessDeniedHandler() {
        return new WebFluxCustomAccessDeniedHandler();
    }

}
