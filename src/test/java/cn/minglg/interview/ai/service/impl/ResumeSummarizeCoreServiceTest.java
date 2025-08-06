package cn.minglg.interview.ai.service.impl;

import cn.hutool.json.JSONUtil;
import cn.minglg.interview.common.utils.FileUtils;
import cn.minglg.interview.minio.service.MinioService;
import cn.minglg.interview.resume.mapper.ResumeMetadataMapper;
import cn.minglg.interview.resume.pojo.ResumeDetail;
import cn.minglg.interview.resume.pojo.ResumeMetadata;
import cn.minglg.interview.resume.repository.ResumeDetailRepository;
import lombok.SneakyThrows;
import org.apache.tika.parser.AutoDetectParser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.InputStream;
import java.util.UUID;

/**
 * ClassName:ResumeSummarizeCoreServiceTest
 * Package:cn.minglg.interview.ai.service.impl
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/1
 * @Version 1.0
 */
@SpringBootTest
public class ResumeSummarizeCoreServiceTest {
    @Autowired
    private MinioService minioService;
    @Autowired
    AutoDetectParser autoDetectParser;
    @Autowired
    private ResumeDetailRepository resumeDetailRepository;
    @Autowired
    private ResumeMetadataMapper resumeMetadataMapper;


    String getFileContent() {
        String bucketName = "resume-upload-4";
        String fileName = "1754140081897.pdf";
        try (InputStream is = minioService.downloadFile(bucketName, fileName)) {
            return FileUtils.getContentFromFile(autoDetectParser,is);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    @Test
    public void testSummarize() {
        Long userId = 4L;
        String taskId = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
        String resumeId = System.currentTimeMillis() + UUID.randomUUID().toString().replace("-", "").substring(0, 15);
        System.out.println("==========异步调用开始==========");
        System.out.println(resumeId);
        String content = getFileContent();
//        resumeSummarizeFacadeService.resumeSummarizeAndSave(userId, taskId, resumeId, content);
        System.out.println("==========异步调用结束==========");
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
        System.out.println(JSONUtil.toJsonStr(metadata));
    }

}
