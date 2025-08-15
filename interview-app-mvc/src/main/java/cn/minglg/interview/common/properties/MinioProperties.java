package cn.minglg.interview.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * ClassName:MinioProperties
 * Package:cn.minglg.interview.common.properties
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/7/27
 * @Version 1.0
 */
@Component
@ConfigurationProperties(prefix = "global.minio")
@Data
public class MinioProperties {
    private String endpoint;
    private String accessKey;
    private String secretKey;
    private Map<String, String> bucketNamePrefix;
}
