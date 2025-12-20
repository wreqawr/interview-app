package cn.minglg.ai.assistant.controller;

import cn.minglg.ai.assistant.service.AssistantService;
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
 * ClassName:AssistantController
 * Package:cn.minglg.interview.ai.controller
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/24
 * @Version 1.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/ai")
public class AssistantController {
    private final AssistantService assistantService;

    /**
     * 处理聊天请求的接口方法
     *
     * @param chatParamMap 包含聊天参数的Map对象，其中应包含：
     *                     - conversationId: 会话ID
     *                     - userMessage: 用户消息内容
     *                     - taskType: 任务类型
     *                     - params: 额外参数Map
     * @return ResponseEntity<GenericResponse<String>> 包含聊天结果的响应实体
     */
    @PostMapping(value = "/chat")
    @SuppressWarnings("unchecked")
    public ResponseEntity<GenericResponse<String>> chat(@RequestBody Map<String, Object> chatParamMap) {
        // 从参数Map中提取聊天所需的基本信息
        String conversationId = (String) chatParamMap.get("conversationId");
        String userMessage = (String) chatParamMap.get("userMessage");
        TaskType taskType = TaskType.fromString((String) chatParamMap.get("taskType"), TaskType.GENERAL_CHAT);

        // 安全地提取参数Map，如果转换失败则使用空Map
        Map<String, Object> params;
        try {
            params = (Map<String, Object>) chatParamMap.get("params");
        } catch (Exception e) {
            params = Map.of();
        }

        // 调用聊天服务处理聊天逻辑并返回响应
        GenericResponse<String> result = assistantService.chat(conversationId, userMessage, taskType, params);
        return ResponseEntity.ok(result);
    }


    /**
     * 助手（处理一般文本任务，不带记忆）
     *
     * @param chatParamMap 包含聊天参数的Map对象，必须包含以下键值：
     *                     - "userMessage": 用户发送的消息内容（String类型）
     *                     - "taskType": 任务类型字符串，将被转换为TaskType枚举（String类型）
     * @return ResponseEntity包装的GenericResponse对象，包含聊天助手的响应结果
     */
    @PostMapping("/assistant")
    public ResponseEntity<GenericResponse<String>> assistant(@RequestBody Map<String, Object> chatParamMap) {
        // 从参数Map中提取聊天所需的基本信息
        String userMessage = (String) chatParamMap.get("userMessage");
        TaskType taskType = TaskType.fromString((String) chatParamMap.get("taskType"), TaskType.GENERAL_CHAT);
        GenericResponse<String> result = assistantService.assistant(userMessage, taskType);
        return ResponseEntity.ok(result);
    }


}
