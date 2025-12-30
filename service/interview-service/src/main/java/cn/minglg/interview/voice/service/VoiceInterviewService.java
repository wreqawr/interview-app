package cn.minglg.interview.voice.service;

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
 * ClassName:VoiceInterviewService
 * Package:cn.minglg.interview.voice.service
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/12/30
 * @Version 1.0
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class VoiceInterviewService {
    private final CandidateServiceFeignClient candidateServiceFeignClient;
    private final ResumeServiceFeignClient resumeServiceFeignClient;
    private final AiServiceFeignClient aiServiceFeignClient;
    private final Executor taskExecutor;

    /**
     * 生成AI代理通话
     * 该方法通过准备聊天参数并调用AI服务来生成AI代理通话
     *
     * @param jobId    职位ID，用于标识特定的职位
     * @param resumeId 简历ID，用于标识特定的简历
     * @return ResponseEntity<?> 包含AI代理通话生成结果的响应实体
     */
    public ResponseEntity<?> generateAIAgentCall(Long jobId, String resumeId) {
        // 准备聊天参数，包括职位ID、简历ID以及相关的服务客户端和任务执行器
        Map<String, Object> chatParamMap = InterviewUtils.prepareChatParams(jobId, resumeId, candidateServiceFeignClient, resumeServiceFeignClient, taskExecutor);
        return aiServiceFeignClient.generateAIAgentCall(chatParamMap);
    }

}
