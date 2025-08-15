package cn.minglg.interview.minio.service.impl;

import cn.minglg.interview.minio.service.MinioService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * ClassName:MinioServiceImplTest
 * Package:cn.minglg.interview.minio.service.impl
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/7/27
 * @Version 1.0
 */
@SpringBootTest
public class MinioServiceImplTest {
    @Autowired
    private MinioService minioService;
    private final String bucketName = "resume-upload-4";

    @SneakyThrows
    @Test
    public void test1() {
        minioService.deleteFile(bucketName, "1753612214298e1845.pdf");
    }

    @SneakyThrows
    @Test
    public void test2() {
        minioService.deleteBucketIfExist(bucketName);
    }
}
