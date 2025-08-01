package cn.minglg.interview.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ClassName:ResumeProperties
 * Package:cn.minglg.interview.properties
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/7/23
 * @Version 1.0
 */
@Component
@ConfigurationProperties(prefix = "global.resume")
@Data
public class ResumeProperties {
    /**
     * 允许上传的文件类型
     */
    private List<String> allowFileTypes;

}
