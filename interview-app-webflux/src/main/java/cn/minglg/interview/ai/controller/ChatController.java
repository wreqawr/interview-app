package cn.minglg.interview.ai.controller;

import cn.minglg.interview.ai.service.ChatService;
import cn.minglg.interview.common.constant.task.TaskType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Map;

/**
 * ClassName:ChatController
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
public class ChatController {
    private final ChatService chatService;

    @PostMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @SuppressWarnings("unchecked")
    public Flux<String> chat(@RequestBody Map<String, Object> chatParamMap) {
        String conversationId = (String) chatParamMap.get("conversationId");
        String userMessage = (String) chatParamMap.get("userMessage");
        TaskType taskType = TaskType.fromString((String) chatParamMap.get("taskType"));
        Map<String, Object> params;
        try {
            params = (Map<String, Object>) chatParamMap.get("params");
        } catch (Exception e) {
            params = Map.of();
        }

        return chatService.chat(conversationId, userMessage, taskType, params);
    }
}
