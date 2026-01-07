package cn.minglg.resume.service;

import cn.minglg.resume.pojo.ResumeMetadata;

/**
 * ClassName:AsyncService
 * Package:cn.minglg.resume.service
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/12/19
 * @Version 1.0
 */
public interface AsyncService {
    void resumeSummarizeAndSave(Long userId,
                                String taskId,
                                String resumeId,
                                String userMessage,
                                ResumeMetadata resumeMetadata);

    void resumeAnalyzeAndSave(Long userId,
                              String taskId,
                              String resumeId);
}
