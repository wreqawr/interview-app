package cn.minglg.interview.text.controller;

import cn.minglg.commons.model.task.TaskType;
import cn.minglg.interview.text.service.TextChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName:TextController
 * Package:cn.minglg.interview.text.controller
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/12/20
 * @Version 1.0
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/interview/textChat")
public class TextChatController {
    private final TextChatService textService;

    /**
     * 准备聊天接口
     *
     * @param chatParamMap 聊天参数映射，包含对话ID、工作ID和简历ID等信息
     * @return ResponseEntity<?> 返回聊天准备结果
     */
    @PostMapping(value = "/prepare")
    public ResponseEntity<?> prepareChat(@RequestBody Map<String, Object> chatParamMap) {
        // 从参数Map中提取聊天所需的基本信息
        log.info("请求进来了");
        String conversationId = (String) chatParamMap.get("conversationId");
        Long jobId = Long.valueOf(String.valueOf(chatParamMap.get("jobId")));
        String resumeId = (String) chatParamMap.get("resumeId");
        log.info("conversationId:{},jobId:{},resumeId:{}", conversationId, jobId, resumeId);
        log.info("现在开始调用service方法");
        return textService.prepareChat(conversationId, jobId, resumeId);
    }

    /**
     * 处理面试进度中的文本聊天请求
     *
     * @param chatParamMap 包含聊天参数的映射表，必须包含conversationId和userMessage两个键
     *                     - conversationId: 会话ID，用于标识当前对话
     *                     - userMessage: 用户发送的消息内容
     * @return ResponseEntity<?> 返回聊天处理结果的响应实体
     */
    @PostMapping(value = "/progress")
    public ResponseEntity<?> textChatInterviewInProgress(@RequestBody Map<String, Object> chatParamMap) {
        // 从参数Map中提取聊天所需的基本信息
        String conversationId = (String) chatParamMap.get("conversationId");
        String userMessage = (String) chatParamMap.get("userMessage");

        // 构建传递给服务层的参数映射表
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("conversationId", conversationId);
        paramMap.put("userMessage", userMessage);
        paramMap.put("taskType", TaskType.MOCK_INTERVIEW);
        paramMap.put("params", null);

        return textService.textChatInterviewInProgress(paramMap);
    }


}
