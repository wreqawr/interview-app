package cn.minglg.resume.feign;

import cn.minglg.commons.model.response.GenericResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * ClassName:AiServletServiceFeignClient
 * Package:cn.minglg.resume.feign
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/12/19
 * @Version 1.0
 */
@FeignClient("ai-servlet-service")
public interface AiServletServiceFeignClient {
    @PostMapping("/api/ai/servlet/chat")
    ResponseEntity<GenericResponse<String>> chat(@RequestBody Map<String, Object> chatParamMap);
}
