package cn.minglg.authentication.config.webflux;

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
     * 创建WebFlux JWT令牌过滤器Bean
     *
     * @param securityProperties 安全配置属性，用于配置JWT验证相关参数
     * @param redisTemplate Redis模板，用于访问Redis存储的令牌黑名单等信息
     * @return WebFluxJwtTokenFilter实例，用于处理JWT令牌验证的WebFlux过滤器
     */
    @Bean
    @ConditionalOnMissingBean
    public WebFluxJwtTokenFilter webFluxJwtTokenFilter(WebFluxSecurityProperties securityProperties,
                                                       ReactiveStringRedisTemplate redisTemplate) {
        return new WebFluxJwtTokenFilter(securityProperties, redisTemplate);
    }


}
