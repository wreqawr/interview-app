package cn.minglg.authentication.autoconfig;

import cn.minglg.authentication.properties.WebFluxSecurityProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * ClassName:WebFluxSecurityAutoConfiguration
 * Package:cn.minglg.authentication.config
 * Description:WebFlux场景下的自动配置类
 *
 * @Author kfzx-minglg
 * @Create 2025/8/16
 * @Version 1.0
 */
@AutoConfiguration
@EnableConfigurationProperties(WebFluxSecurityProperties.class)
@ConditionalOnClass(name = {"org.springframework.web.reactive.DispatcherHandler"})
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class WebFluxSecurityAutoConfiguration {
}
