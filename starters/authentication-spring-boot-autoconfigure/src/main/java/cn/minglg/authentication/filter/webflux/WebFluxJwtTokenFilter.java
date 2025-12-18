package cn.minglg.authentication.filter.webflux;

import cn.minglg.commons.model.context.RequestScopedUserContext;
import cn.minglg.authentication.pojo.SecurityUser;
import cn.minglg.authentication.properties.WebFluxSecurityProperties;
import cn.minglg.authentication.utils.JwtUtils;
import cn.minglg.authentication.utils.WebFluxResponseUtils;
import cn.minglg.commons.model.response.ResponseCode;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.List;

/**
 * ClassName:WebFluxJwtTokenFilter
 * Package:cn.minglg.authentication.filter.webflux
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/20
 * @Version 1.0
 */
@AllArgsConstructor
public class WebFluxJwtTokenFilter implements WebFilter {
    private final WebFluxSecurityProperties securityProperties;
    private final ReactiveStringRedisTemplate redisTemplate;
    private final RequestScopedUserContext userContext;


    /**
     * 过滤器核心方法，用于处理请求的认证和授权逻辑
     *
     * @param exchange 服务器Web交换对象，包含请求和响应信息
     * @param chain    Web过滤器链，用于继续执行后续过滤器
     * @return Mono<Void> 异步处理结果
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        // 预检请求直接放行
        if (exchange.getRequest().getMethod() == HttpMethod.OPTIONS) {
            return chain.filter(exchange);
        }

        // 检查请求路径是否在白名单中
        List<String> whiteListPatterns = securityProperties.getWhiteListPatterns();
        Mono<Boolean> isWhiteList = Mono.just(false);
        if (whiteListPatterns != null && !whiteListPatterns.isEmpty()) {
            isWhiteList = ServerWebExchangeMatchers.pathMatchers(
                            whiteListPatterns.toArray(new String[0])
                    ).matches(exchange)
                    .map(ServerWebExchangeMatcher.MatchResult::isMatch);
        }

        // 根据白名单检查结果和JWT认证结果决定是否放行请求
        return isWhiteList.flatMap(match -> {
            if (Boolean.TRUE.equals(match)) {
                // 白名单路径直接放行
                return chain.filter(exchange);
            }

            // 从请求头获取JWT token
            String token = exchange.getRequest().getHeaders().getFirst("Authorization");
            if (token == null || token.isBlank()) {
                return responseWithUnauthorized(exchange);
            }

            // 验证JWT token的有效性
            SecurityUser user;
            try {
                String secretKey = securityProperties.getJwtSecretKey();
                user = JwtUtils.verifyJwt(token, secretKey);
            } catch (Exception e) {
                return responseWithUnauthorized(exchange);
            }

            // 检查Redis中是否存在对应的认证信息
            String authKey = securityProperties.getAuthKeyPrefix() + ":" + user.getUserId();
            return redisTemplate.opsForValue().get(authKey)
                    .map(token::equals)
                    .defaultIfEmpty(false)
                    .flatMap(valid -> {
                        if (valid) {
                            // 认证成功，设置安全上下文并继续执行
                            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                            Context context = ReactiveSecurityContextHolder
                                    .withAuthentication(authenticationToken);
                            // 设置到请求作用域的用户上下文
                            userContext.setUser(user);
                            return chain.filter(exchange)
                                    .contextWrite(context);
                        }
                        // 认证失败，返回未授权响应
                        return responseWithUnauthorized(exchange);
                    });
        });
    }


    /**
     * 返回未授权响应
     *
     * @param exchange 服务器Web交换对象，用于构建响应
     * @return Mono<Void> 响应式编程的空结果，表示异步操作完成
     */
    private Mono<Void> responseWithUnauthorized(ServerWebExchange exchange) {
        // 构建并返回未授权的JSON响应
        return WebFluxResponseUtils.reactiveResponseWithJson(exchange, ResponseCode.JWT_VERIFY_FAIL, "请先登录！", null);
    }


}
