package cn.minglg.interview.ai.core.resume;

import cn.hutool.json.JSONUtil;
import cn.minglg.interview.auth.pojo.User;
import cn.minglg.interview.common.annotation.TaskHandler;
import cn.minglg.interview.common.constant.TaskType;
import cn.minglg.interview.common.utils.UserUtils;
import cn.minglg.interview.resume.mapper.ResumeMetadataMapper;
import cn.minglg.interview.resume.pojo.ResumeDetail;
import cn.minglg.interview.resume.pojo.ResumeMetadata;
import cn.minglg.interview.resume.repository.ResumeDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.io.Resource;
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
    private final ChatClient chatClient;
    private final ResumeMetadataMapper resumeMetadataMapper;
    private final ResumeDetailRepository resumeDetailRepository;
    private final Map<TaskType, Resource> systemPrompts;

    /**
     * 提取并结构化简历内容
     *
     * @param content 简历内容
     */
    @Async
    @TaskHandler(taskType = TaskType.RESUME_SUMMARIZE)
    public void resumeSummarizeAndSave(String taskId, String resumeId, String content, ResumeMetadata resumeMetadata) {
        // 第一步：获取ai解析结果
        String chatResult = chatClient
                .prompt()
                // 本次对话的系统提示词
                .system(systemPrompts.get(TaskType.RESUME_SUMMARIZE))
                .user(content)
                .call()
                .content();
        ResumeDetail resumeDetail = JSONUtil.toBean(chatResult, ResumeDetail.class);
        // 第二步：mongodb保存解析结果
        User currentUser = UserUtils.getCurrentUser();
        if (currentUser != null) {
            Long userId = currentUser.getUserId();
            resumeDetail.setUserId(userId);
            resumeDetail.setResumeId(resumeId);
            resumeDetail.setRawText(content);
            resumeDetailRepository.save(resumeDetail);
            // 第三步：mysql保存简历元信息
            String resumeTitle = resumeDetail.getBasicInfo().getTargetTitle();
            resumeMetadata.setResumeTitle(resumeTitle);
            resumeMetadataMapper.addResumeMetadata(resumeMetadata);
        }

    }
}
