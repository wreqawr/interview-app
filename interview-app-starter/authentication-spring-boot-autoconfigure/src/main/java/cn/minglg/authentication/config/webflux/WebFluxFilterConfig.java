package cn.minglg.authentication.config.webflux;

import cn.minglg.authentication.filter.webflux.WebFluxJwtTokenFilter;
import cn.minglg.authentication.properties.WebFluxSecurityProperties;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@Configuration
@ConditionalOnClass({ReactiveStringRedisTemplate.class})
public class WebFluxFilterConfig {
    private final WebFluxSecurityProperties securityProperties;
    private final ReactiveStringRedisTemplate redisTemplate;

    /**
     * 创建WebFlux JWT令牌过滤器Bean
     * 该方法用于创建并配置WebFluxJwtTokenFilter实例，用于处理JWT令牌验证和解析。
     * 只有在Spring容器中不存在同类型的Bean时才会创建此Bean。
     *
     * @return WebFluxJwtTokenFilter JWT令牌过滤器实例
     */
    @Bean
    @ConditionalOnMissingBean
    public WebFluxJwtTokenFilter webFluxJwtTokenFilter() {
        return new WebFluxJwtTokenFilter(securityProperties, redisTemplate);
    }

}
