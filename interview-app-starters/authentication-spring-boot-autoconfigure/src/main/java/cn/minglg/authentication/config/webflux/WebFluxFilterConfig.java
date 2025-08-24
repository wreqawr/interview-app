package cn.minglg.authentication.config.webflux;

import cn.minglg.authentication.context.RequestScopedUserContext;
import cn.minglg.authentication.filter.webflux.WebFluxJwtTokenFilter;
import cn.minglg.authentication.properties.WebFluxSecurityProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;

/**
 * ClassName:WebFluxFilterConfig
 * Package:cn.minglg.authentication.config.webflux
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/20
 * @Version 1.0
 */
@Configuration
@ConditionalOnClass({ReactiveStringRedisTemplate.class})
public class WebFluxFilterConfig {

    /**
     * 创建并返回一个请求作用域的用户上下文Bean
     * 该方法用于创建RequestScopedUserContext实例，并将其注册为Spring容器中的Bean。
     * 通过@ConditionalOnMissingBean注解确保只有在容器中不存在同类型Bean时才会创建此Bean，
     * 避免重复定义导致的冲突。
     *
     * @return RequestScopedUserContext 请求作用域的用户上下文实例
     */
    @Bean
    @ConditionalOnMissingBean
    public RequestScopedUserContext requestScopedUserContext() {
        return new RequestScopedUserContext();
    }


    /**
     * 创建WebFlux JWT令牌过滤器Bean
     *
     * @param securityProperties 安全配置属性，用于配置JWT验证相关参数
     * @param redisTemplate      Redis模板，用于访问Redis存储的令牌黑名单等信息
     * @return WebFluxJwtTokenFilter实例，用于处理JWT令牌验证的WebFlux过滤器
     */
    @Bean
    @ConditionalOnMissingBean
    public WebFluxJwtTokenFilter webFluxJwtTokenFilter(WebFluxSecurityProperties securityProperties,
                                                       ReactiveStringRedisTemplate redisTemplate,
                                                       RequestScopedUserContext userContext) {
        return new WebFluxJwtTokenFilter(securityProperties, redisTemplate, userContext);
    }


}
