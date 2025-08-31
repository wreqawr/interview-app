package cn.minglg.interview.interview.chat.controller;

import cn.minglg.interview.interview.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Map;

/**
 * ClassName:AssistantController
 * Package:cn.minglg.interview.interview.chat.controller
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/31
 * @Version 1.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/interview/chat")
public class ChatController {

    private final ChatService chatService;

    /**
     * 准备聊天接口，接收聊天参数并返回聊天准备结果的响应流
     *
     * @param chatParamMap 聊天参数Map，包含conversationId、jobId、resumeId等信息
     * @return Flux<String> 聊天准备结果的响应流
     */
    @PostMapping(value = "/prepare", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> prepareChat(@RequestBody Map<String, Object> chatParamMap) {
        // 从参数Map中提取聊天所需的基本信息
        String conversationId = (String) chatParamMap.get("conversationId");
        String jobId = (String) chatParamMap.get("jobId");
        String resumeId = (String) chatParamMap.get("resumeId");

        // 调用聊天服务处理聊天逻辑并返回响应流
        return chatService.prepareChat(conversationId, jobId, resumeId);
    }

}
