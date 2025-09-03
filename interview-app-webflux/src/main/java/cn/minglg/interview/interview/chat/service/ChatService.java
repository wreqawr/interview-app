package cn.minglg.interview.interview.chat.service;

import cn.minglg.authentication.context.RequestScopedUserContext;
import cn.minglg.authentication.utils.JsonUtils;
import cn.minglg.commons.constant.task.TaskType;
import cn.minglg.interview.ai.service.AssistantService;
import cn.minglg.interview.interview.pojo.Job;
import cn.minglg.interview.interview.pojo.ResumeDetail;
import cn.minglg.interview.interview.repository.CandidateJobsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

/**
 * ClassName:ChatService
 * Package:cn.minglg.interview.interview.chat.service
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/31
 * @Version 1.0
 */
@RequiredArgsConstructor
@Service
public class ChatService {
    private final ReactiveStringRedisTemplate redisTemplate;
    private final ReactiveMongoTemplate mongoTemplate;
    private final CandidateJobsRepository candidateJobsRepository;
    private final RequestScopedUserContext userContext;
    private final AssistantService assistantService;

    /**
     * 准备聊天会话，用于模拟面试场景
     *
     * @param conversationId 会话ID，用于标识唯一的聊天会话
     * @param jobId          职位ID，用于获取职位相关信息
     * @param resumeId       简历ID，用于获取简历文本内容
     * @return Flux<String> 返回聊天响应的流式数据
     */
    public Flux<String> prepareChat(String conversationId, String jobId, String resumeId) {
        return getJobDetails(jobId)
                .flatMapMany(job -> getResumeDetail(resumeId)
                        .flatMapMany(resumeDetail -> {
                            // 构建聊天参数
                            Map<String, Object> params = Map.of(
                                    "companyName", job.getCompanyName(),
                                    "candidateName", resumeDetail.getBasicInfo().getName(),
                                    "jobTitle", job.getJobTitle(),
                                    "jobRequirements", job.getJobRequirements(),
                                    "resumeText", resumeDetail.getRawText(),
                                    "techFocusAreas", job.getTags()
                            );
                            String userMessage = "面试官你好，我是候选人" + params.get("candidateName") + "，在开始之前请先简单介绍一下你自己，然后再开始提问";

                            // 调用AssistantService的chat方法并返回Flux<String>
                            return assistantService.chat(conversationId, userMessage, TaskType.MOCK_INTERVIEW, params);
                        }));
    }


    /**
     * 从Redis中获取职位详情信息
     *
     * @param jobId 职位ID，用于标识特定的职位
     * @return 返回包含职位信息的Mono对象，如果职位未发布则抛出RuntimeException异常
     */
    public Mono<Job> getJobDetails(String jobId) {
        // 构造Redis键值，用于从Redis中获取职位信息
        String redisKey = "jobs:candidate:" + userContext.getUser().getUserId();
        return redisTemplate.opsForHash().get(redisKey, jobId)
                .map(json -> JsonUtils.toBean((String) json, Job.class))
                // 验证职位状态，只允许获取已发布的职位
                .doOnSuccess(job -> {
                    if (job.getStatus() != Job.JobStatus.PUBLISHED) {
                        throw new RuntimeException("职位未发布");
                    }
                })
                // 当Redis中获取失败时，从mysql数据库中查询职位详情并同步到Redis中
                .onErrorResume(throwable -> candidateJobsRepository.findJobDetailsByJobId(Long.valueOf(jobId))
                        .flatMap(job -> {
                            if (job.getStatus() != Job.JobStatus.PUBLISHED) {
                                return Mono.error(new RuntimeException("职位未发布"));
                            }
                            // 将从数据库查询到的职位信息存入Redis缓存
                            return redisTemplate.opsForHash().put(redisKey, jobId, JsonUtils.toJsonStr(job))
                                    .then(redisTemplate.expire(redisKey, Duration.ofMinutes(60)))
                                    .thenReturn(job);
                        })
                );
    }


    /**
     * 根据简历ID获取简历详情信息
     *
     * @param resumeId 简历ID，用于标识特定的简历记录
     * @return 返回包含简历详情信息的Mono对象，如果未找到对应简历则返回错误信息
     */
    public Mono<ResumeDetail> getResumeDetail(String resumeId) {
        // 获取当前登录用户ID
        Long userId = userContext.getUser().getUserId();

        // 构造查询条件：根据用户ID和简历ID查询简历详情
        Query query = Query.query(
                Criteria
                        .where("userId").is(userId)
                        .and("resumeId").is(resumeId)
        );

        // 执行查询并返回简历文本内容
        return mongoTemplate.findOne(query, ResumeDetail.class)
                .switchIfEmpty(Mono.error(new RuntimeException("未找到该简历！")));
    }

}
