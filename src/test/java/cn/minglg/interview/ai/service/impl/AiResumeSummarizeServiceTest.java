package cn.minglg.interview.ai.service.impl;

import cn.minglg.interview.ai.service.AiResumeSummarizeService;
import cn.minglg.interview.minio.service.MinioService;
import cn.minglg.interview.resume.service.ResumeParserService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.InputStream;

/**
 * ClassName:AiResumeSummarizeServiceTest
 * Package:cn.minglg.interview.ai.service.impl
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/1
 * @Version 1.0
 */
@SpringBootTest
public class AiResumeSummarizeServiceTest {
    @Autowired
    private MinioService minioService;
    @Autowired
    private ResumeParserService resumeParserService;
    @Autowired
    private AiResumeSummarizeService aiResumeSummarizeService;


    String getFileContent() {
        String bucketName = "resume-upload";
        String fileName = "17540182116790ef95.pdf";
        try (InputStream is = minioService.downloadFile(bucketName, fileName)) {
            return resumeParserService.parseResume(is);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    @Test
    public void test1() {
        String content = getFileContent();
        String result = aiResumeSummarizeService.resumeSummarize(content);
        System.out.println(result);
    }
}
