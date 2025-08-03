package cn.minglg.interview.ai.facade.resume;

import cn.minglg.interview.ai.core.resume.ResumeSummarizeCoreService;
import cn.minglg.interview.resume.pojo.ResumeDetail;
import cn.minglg.interview.resume.repository.ResumeDetailRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * ClassName:ResumeSummarizeFacadeService
 * Package:cn.minglg.interview.ai.facade.resume
 * Description:简历分析门面类（外部系统唯一入口）
 *
 * @Author kfzx-minglg
 * @Create 2025/8/3
 * @Version 1.0
 */
@Service
public class ResumeSummarizeFacadeService {
    private final ResumeSummarizeCoreService resumeCoreService;
    private final ResumeDetailRepository resumeDetailRepository;

    /**
     * 使用@Lazy解决代理初始化问题
     *
     * @param resumeCoreService 核心业务类
     */
    public ResumeSummarizeFacadeService(
            @Lazy
            ResumeSummarizeCoreService resumeCoreService,
            ResumeDetailRepository resumeDetailRepository) {
        this.resumeCoreService = resumeCoreService;
        this.resumeDetailRepository = resumeDetailRepository;
    }

    /**
     * 简历分析
     *
     * @param userId   用户id
     * @param taskId   任务id
     * @param resumeId 简历id
     * @param content  简历内容
     */
    @Async("taskExecutor")
    public void resumeSummarize(Long userId, String taskId, String resumeId, String content) {
        ResumeDetail resumeDetail = resumeCoreService.resumeSummarize(userId, taskId, resumeId, content);
        resumeDetailRepository.save(resumeDetail);
    }
}
