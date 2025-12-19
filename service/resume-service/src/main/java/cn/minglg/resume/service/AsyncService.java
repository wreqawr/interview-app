package cn.minglg.resume.service;

import cn.minglg.resume.mapper.ResumeMetadataMapper;
import cn.minglg.resume.pojo.ResumeMetadata;
import cn.minglg.resume.properties.ResumeProperties;
import cn.minglg.resume.repository.ResumeDetailRepository;
import org.springframework.data.redis.core.StringRedisTemplate;

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
                                ResumeMetadataMapper resumeMetadataMapper,
                                ResumeDetailRepository resumeDetailRepository,
                                ResumeMetadata resumeMetadata);

    void resumeAnalyzeAndSave(Long userId,
                              String taskId,
                              String resumeId, ResumeProperties resumeProperties,
                              StringRedisTemplate redisTemplate,
                              ResumeMetadataMapper resumeMetadataMapper,
                              ResumeDetailRepository resumeDetailRepository);
}
