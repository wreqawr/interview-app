package cn.minglg.interview.ai.controller;

import cn.minglg.interview.common.annotation.ResponseEntityExceptionHandler;
import cn.minglg.interview.common.constant.response.ResponseCode;
import cn.minglg.interview.common.response.R;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

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
    //private final ChatService chatService;

    @PostMapping(path = "/chat")
    @ResponseEntityExceptionHandler(
            errResponseCode = ResponseCode.AI_SERVICE_ERROR,
            errorMessagePrefix = "AI服务异常")
    public ResponseEntity<R> chatOnline(@RequestBody Map<String, String> paramMap) {
        String conversationId = paramMap.get("conversationId");
        String userMessage = paramMap.get("userMessage");
        //R result = chatService.generalChat(conversationId, userMessage);
        R result = null;
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
