package cn.minglg.resume.config;

import cn.minglg.resume.properties.MinioProperties;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ClassName:MinioConfig
 * Package:cn.minglg.interview.minio.config
 * Description:minio配置类
 *
 * @Author kfzx-minglg
 * @Create 2025/7/27
 * @Version 1.0
 */
@RequiredArgsConstructor
@Configuration
public class MinioConfig {
    private final MinioProperties minioProperties;

    @Bean
    public MinioClient minioClient() {
        String endpoint = minioProperties.getEndpoint();
        String accessKey = minioProperties.getAccessKey();
        String secretKey = minioProperties.getSecretKey();
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }
}
