package cn.minglg.resume.service.impl;

import cn.minglg.commons.annotation.TaskHandler;
import cn.minglg.commons.model.response.GenericResponse;
import cn.minglg.commons.model.resume.ResumeDetail;
import cn.minglg.commons.model.task.TaskStatus;
import cn.minglg.commons.model.task.TaskType;
import cn.minglg.commons.utils.JsonUtils;
import cn.minglg.resume.constants.ResumeConstants;
import cn.minglg.resume.exception.ResumeAnalyzeAndSaveException;
import cn.minglg.resume.feign.AiServiceFeignClient;
import cn.minglg.resume.mapper.ResumeMetadataMapper;
import cn.minglg.resume.pojo.ResumeMetadata;
import cn.minglg.resume.properties.ResumeProperties;
import cn.minglg.resume.repository.ResumeDetailRepository;
import cn.minglg.resume.service.AsyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * ClassName:AsyncServiceImpl
 * Package:cn.minglg.resume.service.impl
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/12/19
 * @Version 1.0
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class AsyncServiceImpl implements AsyncService {
    private final AiServiceFeignClient aiServiceFeignClient;
    private final StringRedisTemplate redisTemplate;
    private final ResumeMetadataMapper resumeMetadataMapper;
    private final ResumeDetailRepository resumeDetailRepository;
    private final ResumeProperties resumeProperties;


    @Override
    @Async("taskExecutor")
    @TaskHandler(taskType = TaskType.RESUME_SUMMARIZE)
    public void resumeSummarizeAndSave(Long userId,
                                       String taskId,
                                       String resumeId,
                                       String userMessage,
                                       ResumeMetadata resumeMetadata) {
        log.info("开始实际执行异步任务resumeSummarizeAndSave，taskId：{}", taskId);
        try {
            // 延时双删确保缓存一致性
            // 第一步：删除redis缓存
            String resumeRedisKeyPrefix = ResumeConstants.RESUME_METADATA_REDIS_KEY_PREFIX;
            redisTemplate.delete(resumeRedisKeyPrefix + ":" + userId);
            // 第二步：获取ai解析结果
            ResponseEntity<GenericResponse<String>> chatResponse = aiServiceFeignClient.chat(Map.of("userMessage", userMessage, "taskType", TaskType.RESUME_SUMMARIZE));
            String chatResult = null;
            if (chatResponse.getBody() != null) {
                chatResult = chatResponse.getBody().getData();
            }
            ResumeDetail resumeDetail = JsonUtils.toBean(chatResult, ResumeDetail.class);

            // 第三步：mongodb保存解析结果
            resumeDetail.setUserId(userId);
            resumeDetail.setResumeId(resumeId);
            resumeDetail.setRawText(userMessage);
            resumeDetailRepository.save(resumeDetail);

            // 第四步：mysql保存简历元信息
            String resumeTitle = resumeDetail.getBasicInfo().getTargetTitle();
            resumeMetadata.setResumeTitle(resumeTitle);
            resumeMetadataMapper.addResumeMetadata(resumeMetadata);
            // 第五步：延时删除redis
            Thread.sleep(200);
            redisTemplate.delete(resumeRedisKeyPrefix + ":" + userId);

        } catch (Exception e) {
            log.error("异步任务resumeSummarizeAndSave执行异常，taskId：{}", taskId);
            throw new ResumeAnalyzeAndSaveException(e.getMessage());
        }
        log.info("异步任务resumeSummarizeAndSave已全部执行完毕，taskId：{}", taskId);
    }

    @Override
    @Async("taskExecutor")
    @TaskHandler(taskType = TaskType.RESUME_ANALYZE)
    public void resumeAnalyzeAndSave(Long userId,
                                     String taskId,
                                     String resumeId) {

        log.info("开始实际执行异步任务resumeAnalyzeAndSave，taskId：{}", taskId);
        ResumeDetail resumeDetail = resumeDetailRepository.findByUserIdAndResumeId(userId, resumeId);
        String redisKey = resumeProperties.getRedisKeyPrefixForAnalyze() + ":" + userId + ":" + resumeId;
        String hashKeyForAnalyze = "analyzeHtmlContent";
        String hashKeyForAnalyzeStatus = "analyzeStatus";
        String hashKeyForAnalyzeTaskId = "analyzeTaskId";
        // 开始前更新简历的分析状态
        try {
            redisTemplate.opsForHash().put(redisKey, hashKeyForAnalyzeStatus, TaskStatus.RUNNING.toString());
            redisTemplate.opsForHash().put(redisKey, hashKeyForAnalyzeTaskId, taskId);
            // 远程调用ai服务
            ResponseEntity<GenericResponse<String>> chatResponse = aiServiceFeignClient.chat(Map.of("userMessage", resumeDetail.getRawText(), "taskType", TaskType.RESUME_ANALYZE));
            String analyzeResult = null;
            if (chatResponse.getBody() != null) {
                analyzeResult = chatResponse.getBody().getData();
            }
            if (analyzeResult != null) {
                redisTemplate.opsForHash().put(redisKey, hashKeyForAnalyze, analyzeResult);
                redisTemplate.opsForHash().put(redisKey, hashKeyForAnalyzeStatus, TaskStatus.FINISHED.toString());
                resumeDetail.setResumeAnalyzeHtmlContentForJobSeekers(analyzeResult);
                resumeDetailRepository.updateResumeAnalyzeHtmlContentForJobSeekersByUserIdAndResumeId(userId, resumeId, analyzeResult);
            }
        } catch (Exception e) {
            redisTemplate.opsForHash().put(redisKey, hashKeyForAnalyzeStatus, TaskStatus.FAILED.toString());
            log.error("异步任务resumeAnalyzeAndSave执行异常，taskId：{}", taskId);
            throw new ResumeAnalyzeAndSaveException(e.getMessage());
        }
        log.info("异步任务resumeAnalyzeAndSave已全部执行完毕，taskId：{}", taskId);
    }
}
