package cn.minglg.interview.utils;

import cn.minglg.commons.model.candidate.Job;
import cn.minglg.commons.model.resume.ResumeDetail;
import cn.minglg.interview.feign.CandidateServiceFeignClient;
import cn.minglg.interview.feign.ResumeServiceFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * ClassName:InterviewUtils
 * Package:cn.minglg.interview.utils
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/12/30
 * @Version 1.0
 */
@Slf4j
public class InterviewUtils {
    /**
     * 准备聊天参数的入口方法
     * 根据是否存在conversationId来决定参数构建策略
     *
     * @param conversationId              对话ID，可选参数
     * @param jobId                       职位ID
     * @param resumeId                    简历ID
     * @param candidateServiceFeignClient 候选人服务Feign客户端
     * @param resumeServiceFeignClient    简历服务Feign客户端
     * @param taskExecutor                任务执行器
     * @return 包含聊天所需参数的Map对象
     */
    public static Map<String, Object> prepareChatParams(String conversationId,
                                                        Long jobId,
                                                        String resumeId,
                                                        CandidateServiceFeignClient candidateServiceFeignClient,
                                                        ResumeServiceFeignClient resumeServiceFeignClient,
                                                        Executor taskExecutor) {
        Map<String, Object> chatMap = new HashMap<>();
        if (StringUtils.hasText(conversationId)) {
            // 当存在对话ID时，将对话ID加入参数并合并其他基础参数
            chatMap.put("conversationId", conversationId);
            chatMap.putAll(prepareChatParams(jobId, resumeId, candidateServiceFeignClient, resumeServiceFeignClient, taskExecutor));
            return chatMap;
        }
        // 当不存在对话ID时，仅构建基础参数
        return prepareChatParams(jobId, resumeId, candidateServiceFeignClient, resumeServiceFeignClient, taskExecutor);
    }


    /**
     * 准备聊天参数，异步获取职位和简历详细信息，并构建用于AI聊天的参数映射
     *
     * @param jobId                       职位ID
     * @param resumeId                    简历ID
     * @param candidateServiceFeignClient 候选人服务Feign客户端
     * @param resumeServiceFeignClient    简历服务Feign客户端
     * @param taskExecutor                任务执行器，用于异步执行
     * @return 包含聊天参数的Map对象，键为"params"，值为具体的参数映射
     */
    public static Map<String, Object> prepareChatParams(Long jobId,
                                                        String resumeId,
                                                        CandidateServiceFeignClient candidateServiceFeignClient,
                                                        ResumeServiceFeignClient resumeServiceFeignClient,
                                                        Executor taskExecutor) {
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
            Map<String, String> extraParams = Map.of(
                    "companyName", job.getCompanyName(),
                    "candidateName", resumeDetail.getBasicInfo().getName(),
                    "jobTitle", job.getJobTitle(),
                    "jobRequirements", job.getJobRequirements(),
                    "resumeText", resumeDetail.getRawText(),
                    "techFocusAreas", job.getTags()
            );
            return Map.<String, Object>of("params", extraParams);
        }).join();
    }

}
