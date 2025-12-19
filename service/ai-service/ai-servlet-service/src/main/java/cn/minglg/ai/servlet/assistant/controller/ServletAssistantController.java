package cn.minglg.ai.servlet.assistant.controller;

import cn.minglg.ai.servlet.assistant.service.ServletAssistantService;
import cn.minglg.commons.model.response.GenericResponse;
import cn.minglg.commons.model.task.TaskType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * ClassName:ServletAssistantController
 * Package:cn.minglg.ai.servlet.assistant.resume.controller
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/12/19
 * @Version 1.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/ai/servlet")
public class ServletAssistantController {
    private final ServletAssistantService assistantService;

    @PostMapping("/chat")
    public ResponseEntity<GenericResponse<String>> chat(@RequestBody Map<String, Object> chatParamMap) {
        // 从参数Map中提取聊天所需的基本信息
        String userMessage = (String) chatParamMap.get("userMessage");
        TaskType taskType = TaskType.fromString((String) chatParamMap.get("taskType"), null);
        GenericResponse<String> result = assistantService.chat(userMessage, taskType);
        return ResponseEntity.ok(result);
    }
}
