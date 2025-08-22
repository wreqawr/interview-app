package cn.minglg.interview;

import com.alibaba.cloud.ai.autoconfigure.dashscope.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ClassName:WebFluxApplication
 * Package:cn.minglg.interview
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/20
 * @Version 1.0
 */
@SpringBootApplication(exclude = {
        DashScopeChatAutoConfiguration.class,
        DashScopeAgentAutoConfiguration.class,
        DashScopeImageAutoConfiguration.class,
        DashScopeAudioSpeechAutoConfiguration.class,
        DashScopeAudioTranscriptionAutoConfiguration.class,
        DashScopeRerankAutoConfiguration.class,
        DashScopeEmbeddingAutoConfiguration.class,
})
public class WebFluxApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebFluxApplication.class, args);
    }
}
