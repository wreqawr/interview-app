package cn.minglg.ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * ClassName:AiApplication
 * Package:cn.minglg.ai
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/12/20
 * @Version 1.0
 */
@SpringBootApplication
@EnableDiscoveryClient
public class AiApplication {
    public static void main(String[] args) {
        SpringApplication.run(AiApplication.class, args);
    }
}
