package cn.minglg.interview.text.service;

import cn.minglg.commons.async.AsyncContextHolder;
import cn.minglg.commons.model.candidate.Job;
import cn.minglg.commons.model.constants.Constants;
import cn.minglg.commons.model.response.GenericResponse;
import cn.minglg.commons.model.resume.ResumeDetail;
import cn.minglg.interview.feign.AiServiceFeignClient;
import cn.minglg.interview.feign.CandidateServiceFeignClient;
import cn.minglg.interview.feign.ResumeServiceFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * ClassName:TextChatService
 * Package:cn.minglg.interview.text.service
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/12/20
 * @Version 1.0
 */
@Slf4j
@Service
public class TextChatService {
    private final CandidateServiceFeignClient candidateServiceFeignClient;
    private final ResumeServiceFeignClient resumeServiceFeignClient;
    private final AiServiceFeignClient aiServiceFeignClient;
    private final Executor taskExecutor;

    public TextChatService(CandidateServiceFeignClient candidateServiceFeignClient,
                           ResumeServiceFeignClient resumeServiceFeignClient,
                           AiServiceFeignClient aiServiceFeignClient,
                           @Qualifier("taskExecutor")
                           Executor taskExecutor) {
        this.candidateServiceFeignClient = candidateServiceFeignClient;
        this.resumeServiceFeignClient = resumeServiceFeignClient;
        this.aiServiceFeignClient = aiServiceFeignClient;
        this.taskExecutor = taskExecutor;
    }

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
        // 异步获取职位详细信息
        CompletableFuture<Job> jobFuture = CompletableFuture.supplyAsync(() -> {
            log.info("{}--->开始获取职位详细信息...", Thread.currentThread().getName());
            return Objects.requireNonNull(candidateServiceFeignClient.getJobDetails(jobId).getBody()).getData();
        }, taskExecutor).exceptionally(e -> {
            log.error("获取职位详细信息失败：{}", e.getMessage());
            return null;
        });

        // 异步获取简历详细信息
        CompletableFuture<ResumeDetail> resumeDetailFuture = CompletableFuture.supplyAsync(() -> {
            log.info("{}--->开始获取简历详细信息...", Thread.currentThread().getName());
            return Objects.requireNonNull(resumeServiceFeignClient.getResumeDetail(resumeId).getBody()).getData();
        }, taskExecutor).exceptionally(e -> {
            log.error("获取简历详细信息失败：{}", e.getMessage());
            return null;
        });

        // 组合两个异步任务的结果，构建聊天参数并调用AI服务准备聊天
        return jobFuture.thenCombine(resumeDetailFuture, (job, resumeDetail) -> {
            // 构建聊天所需参数映射表
            Map<String, Object> chatParamMap = getchatMap(conversationId, job, resumeDetail);
            // 调用远程AI服务执行聊天准备逻辑
            log.info("{}--->开始准备聊天...", Thread.currentThread().getName());
            return aiServiceFeignClient.prepareChat(chatParamMap);
        }).join();

        // 下面是同步版本的实现（已注释），供参考使用
//        Job job = Objects.requireNonNull(candidateServiceFeignClient.getJobDetails(jobId).getBody()).getData();
//        log.info("职位信息如下：{}", job);
//        log.info("开始获取简历详细信息...");
//        ResumeDetail resumeDetail = Objects.requireNonNull(resumeServiceFeignClient.getResumeDetail(resumeId).getBody()).getData();
//        log.info("简历信息如下：{}", resumeDetail);
//        Map<String, Object> chatParamMap = getchatMap(conversationId, job, resumeDetail);
//        log.info("开始准备聊天...");
//        return aiServiceFeignClient.prepareChat(chatParamMap);
    }

    /**
     * 处理面试进行中的文本聊天请求
     *
     * @param chatParamMap 聊天参数映射，包含聊天相关的各种参数配置
     * @return ResponseEntity<GenericResponse<String>> 返回聊天响应结果，包含处理状态和消息内容
     */
    public ResponseEntity<GenericResponse<String>> textChatInterviewInProgress(Map<String, Object> chatParamMap) {
        try {
            // 获取当前请求上下文中的授权令牌并设置到异步上下文中
            if (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes) {
                AsyncContextHolder.setAttribute(Constants.AUTHORIZATION_TOKEN_KEY, attributes.getRequest().getHeader(Constants.AUTHORIZATION_TOKEN_KEY));
            }
            // 调用AI服务Feign客户端进行聊天处理
            return aiServiceFeignClient.chat(chatParamMap);
        } finally {
            // 清理异步上下文，确保资源释放
            AsyncContextHolder.clearContext();
        }
    }


    /**
     * 构造聊天参数映射表
     *
     * @param conversationId 会话ID
     * @param job            职位信息对象
     * @param resumeDetail   简历详情对象
     * @return 包含会话ID和参数映射表的Map对象
     */
    private Map<String, Object> getchatMap(String conversationId, Job job, ResumeDetail resumeDetail) {
        // 构造聊天参数映射表
        Map<String, Object> params = Map.of(
                "companyName", job.getCompanyName(),
                "candidateName", resumeDetail.getBasicInfo().getName(),
                "jobTitle", job.getJobTitle(),
                "jobRequirements", job.getJobRequirements(),
                "resumeText", resumeDetail.getRawText(),
                "techFocusAreas", job.getTags()
        );
        // 返回包含会话ID和参数的映射表
        return Map.of("conversationId", conversationId, "params", params);
    }

}
