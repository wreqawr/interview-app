package org.minglg.authentication.utils;

import cn.minglg.commons.model.response.GenericResponse;
import cn.minglg.commons.model.response.ResponseCode;
import cn.minglg.commons.utils.JsonUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * ClassName:WebFluxResponseUtils
 * Package:cn.minglg.authentication.utils
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/20
 * @Version 1.0
 */
public class WebFluxResponseUtils {
    /**
     * 发送响应结果
     *
     * @param exchange     ServerWebExchange对象，用于获取响应对象
     * @param responseCode 响应状态码枚举
     * @param message      响应消息
     * @param data         响应数据
     * @return Mono<Void> 响应式返回值
     */
    public static Mono<Void> reactiveResponseWithJson(ServerWebExchange exchange, ResponseCode responseCode, String message, Object data) {
        ServerHttpResponse response = exchange.getResponse();
        // 明确设置字符编码为 UTF-8
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        response.getHeaders().set(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");
        // 构建未授权访问的响应结果
        GenericResponse<Object> result = GenericResponse.builder()
                .code(responseCode.getCode())
                .message(message)
                .build();
        if (data != null) {
            result.setData(data);
        }
        // 将响应结果转换为字节数据并写入响应
        byte[] bytes = JsonUtils.toJsonStr(result).getBytes(StandardCharsets.UTF_8);
        DataBuffer dataBuffer = response.bufferFactory().wrap(bytes);
        return response.writeWith(Mono.just(dataBuffer));
    }

}
