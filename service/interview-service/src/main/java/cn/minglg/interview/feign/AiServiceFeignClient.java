package cn.minglg.interview.feign;

import cn.minglg.commons.model.response.GenericResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * ClassName:AiServiceFeignClient
 * Package:cn.minglg.resume.feign
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/12/19
 * @Version 1.0
 */
@FeignClient("ai-service")
public interface AiServiceFeignClient {
    @PostMapping("/api/ai/prepareChat")
    ResponseEntity<GenericResponse<String>> prepareChat(@RequestBody Map<String, Object> chatParamMap);

    @PostMapping("/api/ai/chat")
    ResponseEntity<GenericResponse<String>> chat(@RequestBody Map<String, Object> chatParamMap);
}
