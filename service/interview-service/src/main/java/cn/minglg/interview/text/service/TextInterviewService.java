package cn.minglg.interview.text.service;

import cn.minglg.commons.model.response.GenericResponse;
import cn.minglg.interview.feign.AiServiceFeignClient;
import cn.minglg.interview.feign.CandidateServiceFeignClient;
import cn.minglg.interview.feign.ResumeServiceFeignClient;
import cn.minglg.interview.utils.InterviewUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.Executor;

/**
 * ClassName:TextInterviewService
 * Package:cn.minglg.interview.text.service
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/12/20
 * @Version 1.0
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class TextInterviewService {
    private final CandidateServiceFeignClient candidateServiceFeignClient;
    private final ResumeServiceFeignClient resumeServiceFeignClient;
    private final AiServiceFeignClient aiServiceFeignClient;
    private final Executor taskExecutor;


    /**
     * 准备聊天接口，用于在候选人与职位之间建立AI聊天上下文。
     * 该方法通过异步方式并行获取职位详情和简历详情，并组合构建聊天所需的参数，
     * 最终调用AI服务完成聊天准备。
     *
     * @param conversationId 聊天会话ID，标识一次唯一的对话上下文
     * @param jobId          职位ID，用于获取对应的职位详细信息
     * @param resumeId       简历ID，用于获取对应的简历详细信息
     * @return 返回AI服务准备聊天的结果响应体，包含操作状态及结果信息
     */
    public ResponseEntity<GenericResponse<String>> prepareChat(String conversationId, Long jobId, String resumeId) {
        Map<String, Object> chatParamMap = InterviewUtils.prepareChatParams(conversationId, jobId, resumeId, candidateServiceFeignClient, resumeServiceFeignClient, taskExecutor);
        log.info("{}--->开始准备聊天...", Thread.currentThread().getName());
        return aiServiceFeignClient.prepareChat(chatParamMap);
    }

    /**
     * 处理面试进行中的文本聊天请求
     *
     * @param chatParamMap 聊天参数映射，包含聊天相关的各种参数配置
     * @return ResponseEntity<GenericResponse<String>> 返回聊天响应结果，包含处理状态和消息内容
     */
    public ResponseEntity<GenericResponse<String>> textChatInterviewInProgress(Map<String, Object> chatParamMap) {
        return aiServiceFeignClient.chat(chatParamMap);
    }

}
