package cn.minglg.interview.ai.service.impl;

import cn.minglg.authentication.utils.JsonUtils;
import cn.minglg.interview.ai.core.resume.AiResumeCoreService;
import cn.minglg.interview.resume.mapper.ResumeMetadataMapper;
import cn.minglg.interview.resume.pojo.ResumeDetail;
import cn.minglg.interview.resume.pojo.ResumeMetadata;
import cn.minglg.interview.resume.repository.ResumeDetailRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StringUtils;

/**
 * ClassName:AiResumeCoreServiceTest
 * Package:cn.minglg.interview.ai.service.impl
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/1
 * @Version 1.0
 */
@SpringBootTest
public class AiResumeCoreServiceTest {
    @Autowired
    private ResumeDetailRepository resumeDetailRepository;
    @Autowired
    private ResumeMetadataMapper resumeMetadataMapper;
    @Autowired
    private AiResumeCoreService aiResumeCoreService;

    @SneakyThrows
    @Test
    public void testResumeSummarizeAndSave() {
        Long userId = 4L;
        String resumeId = "1754549458608345c1dca75764ea";
        //String taskId = TaskUtils.generateTaskId();
        ResumeDetail resumeDetail = resumeDetailRepository.findByUserIdAndResumeId(userId, resumeId);
        if (resumeDetail != null && StringUtils.hasText(resumeDetail.getRawText())) {
            System.out.println("==========================");
            //aiResumeCoreService.resumeSummarizeAndSave(userId, taskId, resumeId, resumeDetail.getRawText(), null);
            System.out.println("==========================");
        }
    }

    @Test
    public void testFindByResumeId() {
        String resumeId = "1754272085576f72aefd20103463";
        ResumeDetail byResumeId = resumeDetailRepository.findByResumeId(resumeId);
        System.out.println(byResumeId);
    }

    @Test
    public void testGetResumeAsyncUploadResult() {
        long userId = 4L;
        String resumeId = "1754288236657261b0c7b827c4bf";
        ResumeMetadata metadata = resumeMetadataMapper.getResumeMetadataByUserIdAndResumeId(userId, resumeId);
        System.out.println(JsonUtils.toJsonStr(metadata));
    }

    @Test
    public void testResumeAnalyzeAndSave() {
        Long userId = 4L;
        String resumeId = "1754549458608345c1dca75764ea";
        String taskId = TaskUtils.generateTaskId();
        ResumeDetail resumeDetail = resumeDetailRepository.findByUserIdAndResumeId(userId, resumeId);
        if (resumeDetail != null && StringUtils.hasText(resumeDetail.getRawText())) {
            System.out.println("==========================");
            aiResumeCoreService.resumeAnalyzeAndSave(taskId, resumeId);
            System.out.println("==========================");
        }
    }

}
