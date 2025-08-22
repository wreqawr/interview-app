package cn.minglg.authentication.handler.webflux;

import cn.minglg.authentication.constant.response.ResponseCode;
import cn.minglg.authentication.utils.WebFluxResponseUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * ClassName:WebFluxCustomAccessDeniedHandler
 * Package:cn.minglg.authentication.handler.webflux
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/20
 * @Version 1.0
 */
public class WebFluxCustomAccessDeniedHandler implements ServerAccessDeniedHandler {
    /**
     * 处理访问拒绝异常，当用户权限不足时调用此方法
     *
     * @param exchange 服务器Web交换对象，包含请求和响应信息
     * @param denied   访问拒绝异常对象，包含权限拒绝的具体信息
     * @return 响应式Mono对象，返回空值表示处理完成
     */
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {
        // 构建权限不足的响应信息并返回
        return WebFluxResponseUtils.reactiveResponseWithJson(exchange, ResponseCode.PERMISSION_DENY, "权限不足！", null);
    }

}
