package cn.minglg.interview.ai.core.resume;

import cn.minglg.authentication.context.RequestScopedUserContext;
import cn.minglg.authentication.utils.JsonUtils;
import cn.minglg.commons.model.task.TaskStatus;
import cn.minglg.commons.model.task.TaskType;
import cn.minglg.interview.ai.exception.AiResumeAnalyzeAndSaveException;
import cn.minglg.interview.common.annotation.TaskHandler;
import cn.minglg.interview.common.properties.ResumeProperties;
import cn.minglg.interview.resume.mapper.ResumeMetadataMapper;
import cn.minglg.interview.resume.pojo.ResumeDetail;
import cn.minglg.interview.resume.pojo.ResumeMetadata;
import cn.minglg.interview.resume.repository.ResumeDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * ClassName:AiResumeCoreService
 * Package:cn.minglg.interview.ai.service
 * Description:简历分析核心类
 *
 * @Author kfzx-minglg
 * @Create 2025/7/31
 * @Version 1.0
 */
@RequiredArgsConstructor
@Service
public class AiResumeCoreService {
    private final ResumeProperties resumeProperties;
    private final ChatClient chatClient;
    private final ResumeMetadataMapper resumeMetadataMapper;
    private final ResumeDetailRepository resumeDetailRepository;
    private final StringRedisTemplate redisTemplate;
    private final Map<TaskType, PromptTemplate> systemPromptDynamicTemplate;
    private final RequestScopedUserContext userContext;


    /**
     * 简历摘要处理并保存
     * 该方法通过AI解析简历内容，将解析结果保存到MongoDB，并更新简历元信息到MySQL
     *
     * @param taskId         任务ID
     * @param resumeId       简历ID
     * @param content        简历原始内容
     * @param resumeMetadata 简历元信息对象
     */
    @Async("taskExecutor")
    @TaskHandler(taskType = TaskType.RESUME_SUMMARIZE)
    public void resumeSummarizeAndSave(String taskId, String resumeId, String content, ResumeMetadata resumeMetadata) {
        // 第一步：获取ai解析结果
        String chatResult = chatClient
                .prompt()
                .system(systemPromptDynamicTemplate.get(TaskType.RESUME_SUMMARIZE).render())
                .user(content)
                .call()
                .content();
        ResumeDetail resumeDetail = JsonUtils.toBean(chatResult, ResumeDetail.class);

        // 第二步：mongodb保存解析结果
        resumeDetail.setUserId(userContext.getUser().getUserId());
        resumeDetail.setResumeId(resumeId);
        resumeDetail.setRawText(content);
        resumeDetailRepository.save(resumeDetail);

        // 第三步：mysql保存简历元信息
        String resumeTitle = resumeDetail.getBasicInfo().getTargetTitle();
        resumeMetadata.setResumeTitle(resumeTitle);
        resumeMetadataMapper.addResumeMetadata(resumeMetadata);

    }


    @Async("taskExecutor")
    @TaskHandler(taskType = TaskType.RESUME_ANALYZE)
    public void resumeAnalyzeAndSave(String taskId, String resumeId) {
        Long userId = userContext.getUser().getUserId();
        ResumeDetail resumeDetail = resumeDetailRepository.findByUserIdAndResumeId(userId, resumeId);
        String redisKey = resumeProperties.getRedisKeyPrefixForAnalyze() + ":" + userId + ":" + resumeId;
        String hashKeyForAnalyze = "analyzeHtmlContent";
        String hashKeyForAnalyzeStatus = "analyzeStatus";
        String hashKeyForAnalyzeTaskId = "analyzeTaskId";
        // 开始前更新简历的分析状态
        try {
            redisTemplate.opsForHash().put(redisKey, hashKeyForAnalyzeStatus, TaskStatus.RUNNING.toString());
            redisTemplate.opsForHash().put(redisKey, hashKeyForAnalyzeTaskId, taskId);
            String analyzeResult = chatClient
                    .prompt()
                    // 本次对话的系统提示词
                    .system(systemPromptDynamicTemplate.get(TaskType.RESUME_ANALYZE).render())
                    .user(resumeDetail.getRawText())
                    .call()
                    .content();
            System.out.println(analyzeResult);
            if (analyzeResult != null) {
                redisTemplate.opsForHash().put(redisKey, hashKeyForAnalyze, analyzeResult);
                redisTemplate.opsForHash().put(redisKey, hashKeyForAnalyzeStatus, TaskStatus.FINISHED.toString());
                resumeDetail.setResumeAnalyzeHtmlContentForJobSeekers(analyzeResult);
                resumeDetailRepository.updateResumeAnalyzeHtmlContentForJobSeekersByUserIdAndResumeId(userId, resumeId, analyzeResult);
            }
        } catch (Exception e) {
            redisTemplate.opsForHash().put(redisKey, hashKeyForAnalyzeStatus, TaskStatus.FAILED.toString());
            throw new AiResumeAnalyzeAndSaveException(e.getMessage());
        }
    }

}

