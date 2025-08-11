package cn.minglg.interview.interview.contrller;

import cn.minglg.interview.ai.core.interview.AiInterviewCoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * ClassName:InterviewController
 * Package:cn.minglg.interview.interview
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/11
 * @Version 1.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/interview")
public class InterviewController {

    private final AiInterviewCoreService aiInterviewCoreService;

    @PostMapping("/online")
    public String interviewOnline(@RequestBody Map<String, String> paramMap) {
        String conversationId = paramMap.get("conversationId");
        String userInput = paramMap.get("userInput");
        System.out.println(conversationId);
        System.out.println(userInput);
        return aiInterviewCoreService.interviewOnline(conversationId, userInput);
    }
}
