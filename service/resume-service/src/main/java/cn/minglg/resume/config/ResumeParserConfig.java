package cn.minglg.resume.config;

import org.apache.tika.parser.AutoDetectParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ClassName:ResumeParserConfig
 * Package:cn.minglg.interview.resume.config
 * Description:简历解析器配置类
 *
 * @Author kfzx-minglg
 * @Create 2025/7/26
 * @Version 1.0
 */
@Configuration
public class ResumeParserConfig {


    @Bean
    public AutoDetectParser autoDetectParser() {
        return new AutoDetectParser();
    }

}
