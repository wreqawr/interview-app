package cn.minglg.ai.servlet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * ClassName:AiServletApplication
 * Package:cn.minglg.ai.servlet
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/12/19
 * @Version 1.0
 */
@SpringBootApplication
@EnableDiscoveryClient
public class AiServletApplication {
    public static void main(String[] args) {
        SpringApplication.run(AiServletApplication.class, args);
    }
}
