package cn.minglg.interview.voice.controller;

import cn.minglg.interview.voice.service.VoiceInterviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * ClassName:VoiceInterviewController
 * Package:cn.minglg.interview.voice.controller
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/12/30
 * @Version 1.0
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/interview/voice")
public class VoiceInterviewController {
    private final VoiceInterviewService voiceInterviewService;

    /**
     * 生成AI代理通话的接口
     * 处理前端发送的AI通话生成请求，提取必要的参数并调用服务层方法
     *
     * @param chatParamMap 包含聊天参数的Map对象，必需包含jobId和resumeId字段
     * @return ResponseEntity<?> 通话生成结果的响应实体
     */
    @PostMapping("/generateAIAgentCall")
    public ResponseEntity<?> generateAIAgentCall(@RequestBody Map<String, Object> chatParamMap) {
        // 从参数Map中提取聊天所需的基本信息
        log.info("请求进来了");
        Long jobId = Long.valueOf(String.valueOf(chatParamMap.get("jobId")));
        String resumeId = (String) chatParamMap.get("resumeId");
        log.info("jobId:{},resumeId:{}", jobId, resumeId);
        log.info("现在开始调用service方法");
        return voiceInterviewService.generateAIAgentCall(jobId, resumeId);
    }

}
