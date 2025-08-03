package cn.minglg.interview.ai.core.resume;

import cn.hutool.json.JSONUtil;
import cn.minglg.interview.common.annotation.AsyncTaskHandler;
import cn.minglg.interview.common.constant.TaskType;
import cn.minglg.interview.resume.pojo.ResumeDetail;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * ClassName:ResumeSummarizeCoreService
 * Package:cn.minglg.interview.ai.service
 * Description:简历分析核心类（仅供门面类调用）
 *
 * @Author kfzx-minglg
 * @Create 2025/7/31
 * @Version 1.0
 */
@Service
public class ResumeSummarizeCoreService {
    private final ChatClient chatClient;

    public ResumeSummarizeCoreService(@Qualifier("resumeSummarize") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    /**
     * 简历分析
     *
     * @param userId   用户id
     * @param taskId   任务id
     * @param resumeId 简历id
     * @param content  简历内容
     */
    @AsyncTaskHandler(taskType = TaskType.RESUME_SUMMARIZE)
    public ResumeDetail resumeSummarize(Long userId, String taskId, String resumeId, String content) {
        String chatResult = chatClient
                .prompt(content)
                .call()
                .content();
        ResumeDetail resumeDetail = JSONUtil.toBean(chatResult, ResumeDetail.class);
        resumeDetail.setUserId(userId);
        resumeDetail.setTaskId(taskId);
        resumeDetail.setResumeId(resumeId);
        return resumeDetail;
    }
}
