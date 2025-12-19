package cn.minglg.ai.flux.controller;

import cn.minglg.ai.flux.service.FluxAssistantService;
import cn.minglg.commons.model.task.TaskType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Map;

/**
 * ClassName:FluxAssistantController
 * Package:cn.minglg.interview.ai.controller
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/24
 * @Version 1.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/ai/flux")
public class FluxAssistantController {
    private final FluxAssistantService assistantService;

    /**
     * 处理聊天请求的接口方法
     *
     * @param chatParamMap 包含聊天参数的Map对象，包含以下键值：
     *                     - conversationId: 会话ID
     *                     - userMessage: 用户消息内容
     *                     - taskType: 任务类型
     *                     - params: 额外参数Map
     * @return 返回一个Flux流，包含聊天响应的字符串数据
     */
    @PostMapping(value = "/assistant", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @SuppressWarnings("unchecked")
    public Flux<String> chat(@RequestBody Map<String, Object> chatParamMap) {
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

        // 调用聊天服务处理聊天逻辑并返回响应流
        return assistantService.chat(conversationId, userMessage, taskType, params);
    }

}
