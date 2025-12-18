package cn.minglg.ai.flux;

import com.alibaba.cloud.ai.autoconfigure.dashscope.DashScopeImageAutoConfiguration;
import com.alibaba.cloud.ai.autoconfigure.dashscope.DashScopeVideoAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * ClassName:AiFluxApplication
 * Package:cn.minglg.ai.flux
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/12/18
 * @Version 1.0
 */
@SpringBootApplication(exclude = {
        DashScopeImageAutoConfiguration.class,
        DashScopeVideoAutoConfiguration.class
})
@EnableDiscoveryClient
public class AiFluxApplication {
    public static void main(String[] args) {
        SpringApplication.run(AiFluxApplication.class, args);
    }
}
