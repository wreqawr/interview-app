package cn.minglg.interview.ai.controller;

import cn.minglg.interview.ai.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * ClassName:ChatController
 * Package:cn.minglg.interview.controller
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/13
 * @Version 1.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/ai")
public class ChatController {
    private final ChatService chatService;

    @GetMapping(path = "/chat/{conversationId}/{userMessage}", produces = MediaType.TEXT_HTML_VALUE)
    public Flux<String> chatOnline(@PathVariable("conversationId") String conversationId, @PathVariable("userMessage") String userMessage) {
        return chatService.generalChat(conversationId, userMessage);
    }
}
