package cn.minglg.interview;

import com.alibaba.cloud.ai.autoconfigure.dashscope.DashScopeImageAutoConfiguration;
import com.alibaba.cloud.ai.autoconfigure.dashscope.DashScopeVideoAutoConfiguration;
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
        DashScopeImageAutoConfiguration.class,
        DashScopeVideoAutoConfiguration.class,
//        WebFluxSecurityAutoConfiguration.class
})
//@EnableDiscoveryClient
public class WebFluxApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebFluxApplication.class, args);
    }
}
