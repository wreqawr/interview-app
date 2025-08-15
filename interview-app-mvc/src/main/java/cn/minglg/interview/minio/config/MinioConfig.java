package cn.minglg.interview.minio.config;

import cn.minglg.interview.common.properties.GlobalProperties;
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
    private final GlobalProperties globalProperties;

    @Bean
    public MinioClient minioClient() {
        String endpoint = globalProperties.getMinio().getEndpoint();
        String accessKey = globalProperties.getMinio().getAccessKey();
        String secretKey = globalProperties.getMinio().getSecretKey();
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }
}
